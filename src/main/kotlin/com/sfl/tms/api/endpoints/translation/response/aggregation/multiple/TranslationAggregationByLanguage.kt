package com.sfl.tms.api.endpoints.translation.response.aggregation.multiple

import com.sfl.tms.api.endpoints.translation.request.aggregation.TranslationKeyValuePair

/**
 * User: Vazgen Danielyan
 * Date: 1/18/19
 * Time: 6:45 PM
 */
data class TranslationAggregationByLanguage(val lang: String, val keys: List<TranslationKeyValuePair>)