package com.sfl.tms.rest.common.communicator.translation.response.translation

import com.sfl.tms.rest.common.model.response.AbstractApiResponseModel

/**
 * User: Vazgen Danielyan
 * Date: 12/11/18
 * Time: 6:19 PM
 */
data class TranslatableEntityFieldTranslationResponseModel(var key: String, var value: String, var uuid: String, var label: String, var lang: String) : AbstractApiResponseModel