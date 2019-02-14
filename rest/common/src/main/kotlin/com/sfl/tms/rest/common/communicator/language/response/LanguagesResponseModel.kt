package com.sfl.tms.rest.common.communicator.language.response

import com.sfl.tms.rest.common.model.response.AbstractApiResponseModel

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 1:16 PM
 */
data class LanguagesResponseModel(val languages: List<LanguageResponseModel>) : AbstractApiResponseModel