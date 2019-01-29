package com.sfl.tms.rest.common.communicator.translation.request.translation

import com.sfl.tms.rest.common.communicator.translation.error.TranslationControllerErrorType
import com.sfl.tms.rest.common.communicator.translation.model.TranslatableEntityFieldTypeModel
import com.sfl.tms.rest.common.model.error.ErrorType
import com.sfl.tms.rest.common.model.request.AbstractApiRequestModel
import org.apache.commons.lang3.StringUtils

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 4:22 PM
 */
data class TranslatableEntityFieldTranslationCreateRequestModel(val key: String, val type: TranslatableEntityFieldTypeModel, val value: String, val uuid: String, val label: String, val lang: String) : AbstractApiRequestModel() {
    override fun validateRequiredFields(): List<ErrorType> = ArrayList<ErrorType>()
            .apply {
                if (StringUtils.isEmpty(key)) {
                    add(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_KEY_MISSING)
                }
            }.apply {
                if (StringUtils.isEmpty(uuid)) {
                    add(TranslationControllerErrorType.TRANSLATABLE_ENTITY_UUID_MISSING)
                }
            }.apply {
                if (StringUtils.isEmpty(label)) {
                    add(TranslationControllerErrorType.TRANSLATABLE_ENTITY_LABEL_MISSING)
                }
            }.apply {
                if (StringUtils.isEmpty(value)) {
                    add(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_TRANSLATION_VALUE_MISSING)
                }
            }.apply {
                if (StringUtils.isEmpty(lang)) {
                    add(TranslationControllerErrorType.LANGUAGE_LANG_MISSING)
                }
            }
}