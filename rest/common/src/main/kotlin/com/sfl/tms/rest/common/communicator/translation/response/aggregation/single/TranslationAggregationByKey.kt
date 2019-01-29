package com.sfl.tms.rest.common.communicator.translation.response.aggregation.single

/**
 * User: Vazgen Danielyan
 * Date: 1/18/19
 * Time: 12:39 PM
 */
data class TranslationAggregationByKey(val key: String, val translations: List<TranslationLanguageValuePair>)