package com.sfl.tms.rest.common.communicator.translation.response.field

import com.sfl.tms.rest.common.model.response.AbstractApiResponseModel

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 5:13 PM
 */
data class TranslatableEntityFieldCreateResponseModel(var uuid: String, var name: String) : AbstractApiResponseModel {
    constructor() : this("", "")
}