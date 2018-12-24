package com.sfl.tms.api.endpoints.translation.response.entity

import com.sfl.tms.api.common.model.response.AbstractApiResponseModel

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 5:13 PM
 */
data class TranslatableEntityCreateResponseModel(val uuid: String, val name: String) : AbstractApiResponseModel