package com.sfl.tms.rest.client.language.impl

import com.sfl.tms.rest.client.rs.WebTargetClientService
import com.sfl.tms.rest.client.rs.model.ResponseGenericType
import com.sfl.tms.rest.common.communicator.language.request.LanguageCreateRequestModel
import com.sfl.tms.rest.common.communicator.language.response.LanguagesResponseModel
import com.sfl.tms.rest.common.model.ResultModel
import com.sfl.tms.rest.client.language.LanguageApiClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType

/**
 * User: Vazgen Danielyan
 * Date: 2/11/19
 * Time: 11:38 PM
 */
@Service
class LanguageApiClientImpl : LanguageApiClient {

    //region Injection

    @Autowired
    private lateinit var target: WebTargetClientService

    //endregion

    override fun get(lang: String): ResultModel<LanguagesResponseModel> = target.languageWebTarget
        .also { logger.debug("Calling web target '{}'", it.uri) }
        .queryParam(LANGUAGE_QUERY_PARAMETER, lang)
        .request(MediaType.APPLICATION_JSON)
        .get(ResponseGenericType<ResultModel<LanguagesResponseModel>>())

    override fun create(lang: String): ResultModel<LanguagesResponseModel> = target.languageWebTarget
        .also { logger.debug("Calling web target '{}'", it.uri) }
        .queryParam(LANGUAGE_QUERY_PARAMETER, lang)
        .request(MediaType.APPLICATION_JSON)
        .post(Entity.entity<LanguageCreateRequestModel>(LanguageCreateRequestModel(lang), MediaType.APPLICATION_JSON), ResponseGenericType<ResultModel<LanguagesResponseModel>>())

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(LanguageApiClientImpl::class.java)

        private const val LANGUAGE_QUERY_PARAMETER = "lang"
    }
}