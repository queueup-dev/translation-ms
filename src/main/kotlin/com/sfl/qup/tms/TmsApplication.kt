package com.sfl.qup.tms

import com.google.gson.Gson
import com.sfl.qup.tms.service.language.LanguageService
import com.sfl.qup.tms.service.translatablestatic.TranslatableStaticService
import com.sfl.qup.tms.service.translatablestatic.dto.TranslatableStaticDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class TmsApplication {

    //region Injection

    @Autowired
    private lateinit var translatableStaticService: TranslatableStaticService

    @Autowired
    private lateinit var languageService: LanguageService

    //endregion

    // @Bean
    fun init(): CommandLineRunner = CommandLineRunner {
        val fromJson = Gson().fromJson(TmsApplication::class.java.getResource("/static/locales-en_json.js").readText(), Map::class.java)

        val lang = "en"

        var findByLang = languageService.findByLang(lang)

        if (findByLang == null) {
            findByLang = languageService.create(lang)
        }

        fromJson.forEach { k, v ->
            if (v is Map<*, *>) {
                v.forEach { k2, v2 ->
                    translatableStaticService.create(TranslatableStaticDto(k as String + "_" + k2 as String, v2 as String, findByLang.id))
                }
            } else {
                translatableStaticService.create(TranslatableStaticDto(k as String, v as String, findByLang.id))
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(TmsApplication::class.java, *args)
        }
    }
}
