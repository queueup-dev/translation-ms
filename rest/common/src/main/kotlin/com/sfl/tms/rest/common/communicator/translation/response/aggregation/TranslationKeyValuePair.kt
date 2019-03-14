package com.sfl.tms.rest.common.communicator.translation.response.aggregation

/**
 * User: Vazgen Danielyan
 * Date: 1/18/19
 * Time: 12:40 PM
 */
data class TranslationKeyValuePair(var key: String, var value: String) {
    constructor() : this("", "")
}