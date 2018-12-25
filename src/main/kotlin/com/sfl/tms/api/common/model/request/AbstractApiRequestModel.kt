package com.sfl.tms.api.common.model.request

import com.sfl.tms.api.common.model.AbstractApiModel
import com.sfl.tms.api.common.model.error.ErrorType

/**
 * User: Vazgen Danielyan
 * Date: 9/2/15
 * Time: 7:10 PM
 */
abstract class AbstractApiRequestModel : AbstractApiModel {
    abstract fun validateRequiredFields(): List<ErrorType>
}
