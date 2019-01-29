package com.sfl.tms.rest.common.communicator.translation.response.entity

import com.sfl.tms.rest.common.model.response.AbstractApiResponseModel

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 5:13 PM
 */
data class TranslatableEntityCreateResponseModel(val uuid: String, val label: String, val name: String) : AbstractApiResponseModel