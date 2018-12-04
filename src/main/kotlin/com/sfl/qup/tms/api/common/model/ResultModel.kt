package com.sfl.qup.tms.api.common.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.sfl.qup.tms.api.common.model.error.ErrorModel
import com.sfl.qup.tms.api.common.model.error.type.ErrorType
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

    @JsonProperty(value = "errors")
    var errors: List<ErrorModel>? = null

    //endregion

    //region Constructors

    constructor() {
        this.errors = ArrayList()
    }

    constructor(result: T) : this() {
        this.result = result
    }

    constructor(errors: List<ErrorModel>) {
        this.errors = errors
    }

    //endregion

    //region Public methods

    fun checkIfErrorExists(errorType: ErrorType): Boolean {
        return hasErrors() && errors!!.stream().anyMatch { error -> errorType == error.errorType }
    }

    fun hasErrors(): Boolean {
        return errors != null && !errors!!.isEmpty()
    }

    //endregion

}
