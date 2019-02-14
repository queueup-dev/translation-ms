package com.sfl.tms.rest.client.rs.impl

import com.sfl.tms.rest.client.rs.ClientService
import com.sfl.tms.rest.client.rs.WebTargetClientService
import com.sfl.tms.rest.client.config.TranslationMsClientConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import javax.ws.rs.client.Client
import javax.ws.rs.client.WebTarget

/**
 * User: Vazgen Danielyan
 * Date: 2/11/19
 * Time: 11:24 PM
 */
@Service
class WebTargetClientServiceImpl : WebTargetClientService {

    //region Injection

    @Autowired
    @Qualifier("clientServiceImpl")
    private lateinit var clientService: ClientService

    @Autowired
    private lateinit var configuration: TranslationMsClientConfig

    //endregion

    override val languageWebTarget: WebTarget
        get() = clientService.getClient().target(configuration.languageApi)

    override val translationWebTarget: WebTarget
        get() = clientService.getClient().target(configuration.translationApi)

    override fun getClient(): Client = clientService.getClient()
}