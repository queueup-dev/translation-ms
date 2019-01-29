package com.sfl.tms.rest.common.model.request

import com.sfl.tms.rest.common.model.AbstractApiModel
import com.sfl.tms.rest.common.model.error.ErrorType

/**
 * User: Vazgen Danielyan
 * Date: 9/2/15
 * Time: 7:10 PM
 */
abstract class AbstractApiRequestModel : AbstractApiModel {
    abstract fun validateRequiredFields(): List<ErrorType>
}
