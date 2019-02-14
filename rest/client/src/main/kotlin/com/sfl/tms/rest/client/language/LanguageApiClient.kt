package com.sfl.tms.rest.client.language

import com.sfl.tms.rest.common.communicator.language.response.LanguagesResponseModel
import com.sfl.tms.rest.common.model.ResultModel

/**
 * User: Vazgen Danielyan
 * Date: 1/26/19
 * Time: 11:05 PM
 */
interface LanguageApiClient {
    fun get(lang: String): ResultModel<LanguagesResponseModel>

    fun create(lang: String): ResultModel<LanguagesResponseModel>
}