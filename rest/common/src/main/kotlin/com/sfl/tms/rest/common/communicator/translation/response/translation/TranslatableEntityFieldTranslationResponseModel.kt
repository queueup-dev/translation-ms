package com.sfl.tms.rest.common.communicator.translation.response.translation

import com.sfl.tms.rest.common.model.response.AbstractApiResponseModel

/**
 * User: Vazgen Danielyan
 * Date: 12/11/18
 * Time: 6:19 PM
 */
data class TranslatableEntityFieldTranslationResponseModel(val key: String, val value: String, val uuid: String, val label: String, val lang: String) : AbstractApiResponseModel