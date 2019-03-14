package com.sfl.tms.rest.common.communicator.translation.request.aggregation.multiple

import com.sfl.tms.rest.common.communicator.translation.error.TranslationControllerErrorType
import com.sfl.tms.rest.common.communicator.translation.request.aggregation.TranslationKeyValuePair
import com.sfl.tms.rest.common.model.error.ErrorType
import com.sfl.tms.rest.common.model.request.AbstractApiRequestModel
import org.apache.commons.lang3.StringUtils

/**
 * User: Vazgen Danielyan
 * Date: 1/18/19
 * Time: 6:45 PM
 */
data class TranslationAggregationByLanguage(var lang: String, var keys: List<TranslationKeyValuePair>) : AbstractApiRequestModel() {

    constructor(): this("", listOf())

    override fun validateRequiredFields(): List<ErrorType> = ArrayList<ErrorType>()
            .apply {
                if (StringUtils.isEmpty(lang)) {
                    add(TranslationControllerErrorType.LANGUAGE_LANG_MISSING)
                }
            }.apply {
                keys.map { it.validateRequiredFields() }.flatten().distinct().let { addAll(it) }
            }

}
