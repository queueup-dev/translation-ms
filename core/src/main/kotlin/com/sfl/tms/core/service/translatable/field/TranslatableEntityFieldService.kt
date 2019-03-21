package com.sfl.tms.core.service.translatable.field

import com.sfl.tms.core.domain.translatable.TranslatableEntityField
import com.sfl.tms.core.domain.translatable.TranslatableEntityFieldType
import com.sfl.tms.core.service.translatable.field.dto.TranslatableEntityFieldDto

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:11 PM
 */
interface TranslatableEntityFieldService {

    fun findByKeyAndTypeAndEntity(key: String, type: TranslatableEntityFieldType, uuid: String, label: String): TranslatableEntityField?

    fun getByKeyAndTypeAndEntity(key: String, type: TranslatableEntityFieldType, uuid: String, label: String): TranslatableEntityField

    fun create(dto: TranslatableEntityFieldDto): TranslatableEntityField
}