package com.sfl.qup.tms.api.endpoints.translation.response.entity

import com.sfl.qup.tms.api.common.model.response.AbstractApiResponseModel

/**
 * User: Vazgen Danielyan
 * Date: 12/6/18
 * Time: 3:51 PM
 */
data class TranslatableEntityTranslationsCreateResponseModel(val uuid: String, val translations: List<TranslatableEntityTranslationCreateResponseModel>) : AbstractApiResponseModel