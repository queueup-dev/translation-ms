package com.sfl.qup.tms.api.endpoints.language

import com.sfl.qup.tms.api.common.model.AbstractApiModel
import com.sfl.qup.tms.api.common.model.ResultModel
import com.sfl.qup.tms.api.common.model.error.type.EntityNotFoundErrorModel
import com.sfl.qup.tms.api.endpoints.AbstractBaseController.Companion.created
import com.sfl.qup.tms.api.endpoints.AbstractBaseController.Companion.notFound
import com.sfl.qup.tms.api.endpoints.AbstractBaseController.Companion.ok
import com.sfl.qup.tms.api.endpoints.language.model.response.LanguageResponseModel
import com.sfl.qup.tms.service.language.LanguageService
import com.sfl.qup.tms.service.language.exception.LanguageNotFoundByLangException
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
    fun get(@RequestParam("lang") lang: String): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        languageService.get(lang).let { ok(LanguageResponseModel(it.lang)) }
    } catch (e: LanguageNotFoundByLangException) {
        notFound(EntityNotFoundErrorModel(e.localizedMessage))
    }

    @RequestMapping(value = ["/"], method = [RequestMethod.POST])
    fun create(@RequestParam("lang") lang: String): ResponseEntity<ResultModel<out AbstractApiModel>> = languageService.create(lang).let { created(LanguageResponseModel(it.lang)) }

}