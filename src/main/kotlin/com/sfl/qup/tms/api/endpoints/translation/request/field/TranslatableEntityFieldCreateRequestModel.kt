package com.sfl.qup.tms.api.endpoints.translation.request.field

import com.sfl.qup.tms.api.common.model.error.ErrorModel
import com.sfl.qup.tms.api.common.model.request.AbstractApiRequestModel
import com.sfl.qup.tms.api.endpoints.translation.error.TranslationControllerErrorType
import org.apache.commons.lang3.StringUtils

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 4:22 PM
 */
data class TranslatableEntityFieldCreateRequestModel(val entityUuid: String, val fieldName: String) : AbstractApiRequestModel() {
    override fun validateRequiredFields(): List<ErrorModel> = ArrayList<ErrorModel>()
            .apply {
                if (StringUtils.isEmpty(entityUuid)) {
                    add(ErrorModel(TranslationControllerErrorType.TRANSLATABLE_ENTITY_UUID_MISSING))
                }
            }.apply {
                if (StringUtils.isEmpty(fieldName)) {
                    add(ErrorModel(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_NAME_MISSING))
                }
            }
}