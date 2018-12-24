package com.sfl.tms.api.endpoints.language.model.response

import com.sfl.tms.api.common.model.response.AbstractApiResponseModel

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 1:16 PM
 */
data class LanguagesResponseModel(val languages: List<LanguageResponseModel>) : AbstractApiResponseModel