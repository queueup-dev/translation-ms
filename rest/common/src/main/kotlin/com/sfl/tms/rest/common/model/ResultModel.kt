package com.sfl.tms.rest.common.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.sfl.tms.rest.common.model.error.ErrorType
import java.util.*

/**
 * User: Vazgen Danielyan
 * Date: 9/1/15
 * Time: 10:54 PM
 */
class ResultModel<T : AbstractApiModel> : AbstractApiModel {

    //region Properties

    @JsonProperty
    var result: T? = null

    @JsonValue
    var errors: List<ErrorType>? = null

    @JsonProperty(value = "success")
    val success: Boolean = !hasErrors()

    //endregion

    //region Constructors

    constructor() {
        this.errors = ArrayList()
    }

    constructor(result: T) : this() {
        this.result = result
    }

    constructor(errors: List<ErrorType>) {
        this.errors = errors
    }

    //endregion

    //region Public methods

    fun checkIfErrorExists(errorType: ErrorType): Boolean = hasErrors() && errors!!.stream().anyMatch { error -> errorType == error }

    fun hasErrors(): Boolean = errors != null && !errors!!.isEmpty()

    //endregion

}
