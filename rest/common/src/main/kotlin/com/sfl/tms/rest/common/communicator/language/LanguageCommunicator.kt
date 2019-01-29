package com.sfl.tms.rest.common.communicator.language

import com.sfl.tms.rest.common.model.AbstractApiModel
import com.sfl.tms.rest.common.model.ResultModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

/**
 * User: Vazgen Danielyan
 * Date: 1/26/19
 * Time: 11:01 PM
 */
@RequestMapping("/language")
interface LanguageCommunicator {
    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun get(@RequestParam("lang", required = false) lang: String?): ResponseEntity<ResultModel<out AbstractApiModel>>

    @RequestMapping(value = ["/"], method = [RequestMethod.POST])
    fun create(@RequestParam("lang") lang: String): ResponseEntity<ResultModel<out AbstractApiModel>>
}