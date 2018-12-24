package com.sfl.tms.api.endpoints.translation.request.entity

import com.sfl.tms.api.common.model.error.ErrorModel
import com.sfl.tms.api.common.model.request.AbstractApiRequestModel
import com.sfl.tms.api.endpoints.translation.error.TranslationControllerErrorType
import org.apache.commons.lang3.StringUtils

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 4:22 PM
 */
data class TranslatableEntityCreateRequestModel(val uuid: String, val name: String) : AbstractApiRequestModel() {
    override fun validateRequiredFields(): List<ErrorModel> = ArrayList<ErrorModel>()
            .apply {
                if (StringUtils.isEmpty(uuid)) {
                    add(ErrorModel(TranslationControllerErrorType.TRANSLATABLE_ENTITY_UUID_MISSING))
                }
            }.apply {
                if (StringUtils.isEmpty(name)) {
                    add(ErrorModel(TranslationControllerErrorType.TRANSLATABLE_ENTITY_NAME_MISSING))
                }
            }

}