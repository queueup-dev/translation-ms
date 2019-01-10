package com.sfl.tms

import com.google.gson.Gson
import com.sfl.tms.service.language.LanguageService
import com.sfl.tms.service.translatable.entity.TranslatableEntityService
import com.sfl.tms.service.translatable.entity.dto.TranslatableEntityDto
import com.sfl.tms.service.translatablestatic.TranslatableStaticService
import com.sfl.tms.service.translatablestatic.dto.TranslatableStaticDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import java.util.*

@EnableCaching
@SpringBootApplication
class TmsApplication {

    //region Injection

    @Autowired
    private lateinit var translatableEntityService: TranslatableEntityService

    @Autowired
    private lateinit var translatableStaticService: TranslatableStaticService

    @Autowired
    private lateinit var languageService: LanguageService

    //endregion

    @Bean
    fun init(): CommandLineRunner = CommandLineRunner {

        createLanguageIfNotExist("en")
        createLanguageIfNotExist("nl")

        var entity = translatableEntityService.findByUuid(templateUuid)

        if (entity == null) {
            entity = translatableEntityService.createTemplate(TranslatableEntityDto(UUID.fromString(templateUuid).toString(), "Translatable entity template"))
        }

        insertData("en", entity.uuid)
        insertData("nl", entity.uuid)
    }

    private fun createLanguageIfNotExist(lang: String) {
        if (languageService.findByLang(lang) == null) {
            languageService.create(lang)
        }
    }

    private fun insertData(lang: String, uuid: String) {
        val fromJson = Gson().fromJson(TmsApplication::class.java.getResource("/static/locales-$lang.json").readText(), Map::class.java)

        fromJson.forEach { k, v ->
            val key = k as String

            if (v is Map<*, *>) {
                v.forEach { k2, value ->
                    createOrUpdateIfExist(key + "." + k2 as String, uuid, lang, value as String)
                }
            } else {
                createOrUpdateIfExist(key, uuid, lang, v as String)
            }
        }
    }

    private fun createOrUpdateIfExist(key: String, uuid: String, lang: String, value: String) = TranslatableStaticDto(key, uuid, value, lang).let {
        if (translatableStaticService.findByKeyAndEntityUuidAndLanguageLang(it.key, uuid, it.lang) == null) {
            translatableStaticService.create(it)
        } else {
            translatableStaticService.updateValue(it)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(TmsApplication::class.java, *args)
        }

        const val templateUuid = "00000000-0000-0000-0000-000000000000"
    }
}
