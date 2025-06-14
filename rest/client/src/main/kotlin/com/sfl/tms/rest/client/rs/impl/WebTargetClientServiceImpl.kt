package com.sfl.tms.rest.client.rs.impl

import com.sfl.tms.rest.client.rs.TranslationClientService
import com.sfl.tms.rest.client.rs.WebTargetClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import jakarta.ws.rs.client.Client
import jakarta.ws.rs.client.WebTarget

/**
 * User: Vazgen Danielyan
 * Date: 2/11/19
 * Time: 11:24 PM
 */
@Service
open class WebTargetClientServiceImpl : WebTargetClientService {

    //region Injection

    @Autowired
    @Qualifier("translationClientServiceImpl")
    private lateinit var clientService: TranslationClientService

    @Value("\${translation.host}")
    private lateinit var host: String

    //endregion

    override val languageWebTarget: WebTarget
        get() = clientService.getClient().target("$host/language")

    override val translationWebTarget: WebTarget
        get() = clientService.getClient().target("$host/translation")

    override fun getClient(): Client = clientService.getClient()
}