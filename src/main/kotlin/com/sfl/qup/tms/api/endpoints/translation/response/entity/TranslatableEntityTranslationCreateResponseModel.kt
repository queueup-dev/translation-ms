package com.sfl.qup.tms.api.endpoints.translation.response.entity

import com.sfl.qup.tms.api.common.model.response.AbstractApiResponseModel

/**
 * User: Vazgen Danielyan
 * Date: 12/6/18
 * Time: 4:01 PM
 */
data class TranslatableEntityTranslationCreateResponseModel(val text: String, val lang: String) : AbstractApiResponseModel