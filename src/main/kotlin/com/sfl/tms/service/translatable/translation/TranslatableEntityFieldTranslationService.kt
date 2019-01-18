package com.sfl.tms.service.translatable.translation

import com.sfl.tms.domain.translatable.TranslatableEntityFieldTranslation
import com.sfl.tms.domain.translatable.TranslatableEntityFieldType
import com.sfl.tms.service.translatable.translation.dto.TranslatableEntityFieldTranslationDto

/**
 * User: Vazgen Danielyan
 * Date: 1/15/19
 * Time: 6:31 PM
 */
interface TranslatableEntityFieldTranslationService {

    fun findByFieldAndLanguage(key: String, type: TranslatableEntityFieldType, uuid: String, label: String, lang: String): TranslatableEntityFieldTranslation?

    fun getByFieldAndLanguage(key: String, type: TranslatableEntityFieldType, uuid: String, label: String, lang: String): TranslatableEntityFieldTranslation

    fun getByKeyAndEntity(key: String, type: TranslatableEntityFieldType, uuid: String, label: String): List<TranslatableEntityFieldTranslation>

    fun create(dto: TranslatableEntityFieldTranslationDto): TranslatableEntityFieldTranslation

    fun updateValue(dto: TranslatableEntityFieldTranslationDto): TranslatableEntityFieldTranslation

}