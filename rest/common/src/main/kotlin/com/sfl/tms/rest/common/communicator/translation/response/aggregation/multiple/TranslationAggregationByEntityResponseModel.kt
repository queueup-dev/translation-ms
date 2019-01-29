package com.sfl.tms.rest.common.communicator.translation.response.aggregation.multiple

import com.sfl.tms.rest.common.model.response.AbstractApiResponseModel
import com.sfl.tms.rest.common.communicator.translation.request.aggregation.multiple.TranslationAggregationByLanguage

/**
 * User: Vazgen Danielyan
 * Date: 1/18/19
 * Time: 12:39 PM
 */
data class TranslationAggregationByEntityResponseModel(val uuid: String, val label: String, val languages: List<TranslationAggregationByLanguage>) : AbstractApiResponseModel