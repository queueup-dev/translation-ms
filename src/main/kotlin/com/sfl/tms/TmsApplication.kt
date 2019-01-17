package com.sfl.tms

import com.google.gson.Gson
import com.sfl.tms.service.language.LanguageService
import com.sfl.tms.service.translatable.entity.TranslatableEntityService
import com.sfl.tms.service.translatable.entity.dto.TranslatableEntityDto
import com.sfl.tms.service.translatable.field.TranslatableEntityFieldService
import com.sfl.tms.service.translatable.field.dto.TranslatableEntityFieldDto
import com.sfl.tms.service.translatable.translation.TranslatableEntityFieldTranslationService
import com.sfl.tms.service.translatable.translation.dto.TranslatableEntityFieldTranslationDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import java.util.*

@SpringBootApplication
class TmsApplication {

    //region Injection

    @Autowired
    private lateinit var languageService: LanguageService

    @Autowired
    private lateinit var translatableEntityService: TranslatableEntityService

    @Autowired
    private lateinit var translatableEntityFieldService: TranslatableEntityFieldService

    @Autowired
    private lateinit var translatableEntityFieldTranslationService: TranslatableEntityFieldTranslationService

    //endregion

    @Bean
    fun init(): CommandLineRunner = CommandLineRunner {

        createLanguageIfNotExist("en")
        createLanguageIfNotExist("nl")

        translatableEntityService.findByUuidAndLabel(templateUuid, templateLabel)
                ?: translatableEntityService.create(TranslatableEntityDto(UUID.fromString(templateUuid).toString(), templateLabel, "Translatable entity template"))

        insertData("en", templateUuid, templateLabel)
        insertData("nl", templateUuid, templateLabel)
    }

    //region Utility methods

    private fun createLanguageIfNotExist(lang: String) = languageService.findByLang(lang) ?: languageService.create(lang)

    private fun insertData(lang: String, uuid: String, label: String) = Gson()
            .fromJson(TmsApplication::class.java.getResource("/static/locales-$lang.json").readText(), Map::class.java)
            .forEach { k, v ->
                val key = k as String

                if (v is Map<*, *>) {
                    v.forEach { k2, value ->
                        createOrUpdateIfExist(key + "." + k2 as String, value as String, uuid, label, lang)
                    }
                } else {
                    createOrUpdateIfExist(key, v as String, uuid, label, lang)
                }
            }


    private fun createOrUpdateIfExist(key: String, value: String, uuid: String, label: String, lang: String) {

        translatableEntityFieldService.findByKeyAndEntity(key, uuid, label) ?: translatableEntityFieldService.create(TranslatableEntityFieldDto(key, uuid, label))

        translatableEntityFieldTranslationService.findByFieldAndLanguage(key, uuid, label, lang).let {

            val dto = TranslatableEntityFieldTranslationDto(key, value, uuid, label, lang)

            if (it == null) {
                translatableEntityFieldTranslationService.create(dto)
            } else {
                translatableEntityFieldTranslationService.updateValue(dto)
            }
        }
    }

    //endregion

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(TmsApplication::class.java, *args)
        }

        private const val templateUuid = "00000000-0000-0000-0000-000000000000"
        private const val templateLabel = "00000000-0000-0000-0000-000000000000"
    }
}
