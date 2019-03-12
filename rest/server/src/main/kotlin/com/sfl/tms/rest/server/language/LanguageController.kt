package com.sfl.tms.rest.server.language

import com.sfl.tms.core.service.language.LanguageService
import com.sfl.tms.core.service.language.exception.LanguageExistByLangException
import com.sfl.tms.core.service.language.exception.LanguageNotFoundByLangException
import com.sfl.tms.rest.common.communicator.language.request.LanguageCreateRequestModel
import com.sfl.tms.rest.common.communicator.language.response.LanguageResponseModel
import com.sfl.tms.rest.common.communicator.translation.error.TranslationControllerErrorType
import com.sfl.tms.rest.common.model.AbstractApiModel
import com.sfl.tms.rest.common.model.ResultModel
import com.sfl.tms.rest.server.AbstractBaseController
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 1:10 PM
 */
@RestController
@RequestMapping("/language")
class LanguageController : AbstractBaseController() {

    //region Injection

    @Autowired
    private lateinit var languageService: LanguageService

    //endregion

    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun get(@RequestParam("lang") lang: String): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        lang
            .also { logger.trace("Retrieving language for provided lang - {}", lang) }
            .let {
                languageService.getByLang(it)
            }
            .let { ok(LanguageResponseModel(it.lang)) }
            .also { logger.debug("Retrieved language for provided lang - {} ", lang) }
    } catch (e: LanguageNotFoundByLangException) {
        notFound(TranslationControllerErrorType.LANGUAGE_NOT_FOUND_BY_LANG_EXCEPTION)
    }

    @RequestMapping(value = ["/"], method = [RequestMethod.POST])
    fun create(@RequestBody request: LanguageCreateRequestModel): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        request
            .also { logger.trace("Creating new language for provided lang - {} ", it.lang) }
            .let { languageService.create(it.lang) }
            .let { created(LanguageResponseModel(it.lang)) }
            .also { logger.debug("Successfully created language for provided lang - {} ", request.lang) }
    } catch (e: LanguageExistByLangException) {
        internal(TranslationControllerErrorType.LANGUAGE_EXIST_BY_LANG_EXCEPTION)
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(LanguageController::class.java)
    }
}