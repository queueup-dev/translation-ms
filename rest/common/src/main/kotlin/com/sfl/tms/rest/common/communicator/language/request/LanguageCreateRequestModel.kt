package com.sfl.tms.rest.common.communicator.language.request

import com.sfl.tms.rest.common.communicator.language.error.LanguageControllerErrorType
import com.sfl.tms.rest.common.model.error.ErrorType
import com.sfl.tms.rest.common.model.request.AbstractApiRequestModel
import org.apache.commons.lang3.StringUtils

/**
 * User: Vazgen Danielyan
 * Date: 2/12/19
 * Time: 12:25 AM
 */
data class LanguageCreateRequestModel(val lang: String) : AbstractApiRequestModel() {
    override fun validateRequiredFields(): List<ErrorType> = ArrayList<ErrorType>()
        .apply {
            if (StringUtils.isEmpty(lang)) {
                add(LanguageControllerErrorType.LANGUAGE_LANG_MISSING)
            }
        }
}