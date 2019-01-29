package com.sfl.tms.rest.server.language

import com.sfl.tms.core.service.language.LanguageService
import com.sfl.tms.core.service.language.exception.LanguageExistByLangException
import com.sfl.tms.core.service.language.exception.LanguageNotFoundByLangException
import com.sfl.tms.rest.common.communicator.language.LanguageCommunicator
import com.sfl.tms.rest.common.model.AbstractApiModel
import com.sfl.tms.rest.common.model.ResultModel
import com.sfl.tms.rest.server.AbstractBaseController
import com.sfl.tms.rest.server.language.model.response.LanguageResponseModel
import com.sfl.tms.rest.server.language.model.response.LanguagesResponseModel
import com.sfl.tms.rest.common.communicator.translation.error.TranslationControllerErrorType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 1:10 PM
 */
@RestController
class LanguageController : LanguageCommunicator, AbstractBaseController() {

    //region Injection

    @Autowired
    private lateinit var languageService: LanguageService

    //endregion

    override fun get(lang: String?): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        lang
                .also { logger.trace("Retrieving language for provided lang - {}", lang) }
                .let {
                    if (it == null) {
                        languageService.getAll().map { LanguageResponseModel(it.lang) }.let { ok(LanguagesResponseModel(it)) }
                    } else {
                        languageService.getByLang(it).let { ok(LanguageResponseModel(it.lang)) }
                    }
                }
                .also { logger.debug("Retrieved language for provided lang - {} ", lang) }
    } catch (e: LanguageNotFoundByLangException) {
        notFound(TranslationControllerErrorType.LANGUAGE_NOT_FOUND_BY_LANG_EXCEPTION)
    }

    override fun create(lang: String): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        lang
                .also { logger.trace("Creating new language for provided lang - {} ", lang) }
                .let { languageService.create(it) }
                .let { created(LanguageResponseModel(it.lang)) }
                .also { logger.debug("Successfully created language for provided lang - {} ", lang) }
    } catch (e: LanguageExistByLangException) {
        internal(TranslationControllerErrorType.LANGUAGE_EXIST_BY_LANG_EXCEPTION)
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(LanguageController::class.java)
    }
}