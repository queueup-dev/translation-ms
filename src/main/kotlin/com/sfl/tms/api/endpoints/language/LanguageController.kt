package com.sfl.tms.api.endpoints.language

import com.sfl.tms.api.common.model.AbstractApiModel
import com.sfl.tms.api.common.model.ResultModel
import com.sfl.tms.api.endpoints.AbstractBaseController.Companion.created
import com.sfl.tms.api.endpoints.AbstractBaseController.Companion.notFound
import com.sfl.tms.api.endpoints.AbstractBaseController.Companion.ok
import com.sfl.tms.api.endpoints.language.model.response.LanguageResponseModel
import com.sfl.tms.api.endpoints.language.model.response.LanguagesResponseModel
import com.sfl.tms.api.endpoints.translation.error.TranslationControllerErrorType
import com.sfl.tms.service.language.LanguageService
import com.sfl.tms.service.language.exception.LanguageNotFoundByLangException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 1:10 PM
 */
@RestController
@RequestMapping("/language")
class LanguageController {

    //region Injection

    @Autowired
    private lateinit var languageService: LanguageService

    //endregion

    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun get(@RequestParam("lang", required = false) lang: String?): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        logger.trace("Retrieving language for provided lang - {}", lang)
        if (lang == null) {
            languageService.getAll().map { LanguageResponseModel(it.lang) }.let { ok(LanguagesResponseModel(it)) }
        } else {
            languageService.getByLang(lang).let { ok(LanguageResponseModel(it.lang)) }
        }
                .also { logger.debug("Retrieved language for provided lang - {} ", lang) }
    } catch (e: LanguageNotFoundByLangException) {
        notFound(TranslationControllerErrorType.LANGUAGE_NOT_FOUND_BY_LANG_EXCEPTION)
    }

    @RequestMapping(value = ["/"], method = [RequestMethod.POST])
    fun create(@RequestParam("lang") lang: String): ResponseEntity<ResultModel<out AbstractApiModel>> = lang
            .also { logger.trace("Creating new language for provided lang - {} ", lang) }
            .let { languageService.create(it) }
            .let { created(LanguageResponseModel(it.lang)) }
            .also { logger.debug("Successfully created language for provided lang - {} ", lang) }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(LanguageController::class.java)
    }
}