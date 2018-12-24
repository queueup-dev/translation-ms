package com.sfl.tms.api.common.model.error

import com.fasterxml.jackson.annotation.JsonProperty
import com.sfl.tms.api.common.model.AbstractApiModel
import com.sfl.tms.api.common.model.error.type.ErrorType

/**
 * User: Vazgen Danielyan
 * Date: 9/1/15
 * Time: 10:53 PM
 */
class ErrorModel(@JsonProperty val errorType: ErrorType) : AbstractApiModel
