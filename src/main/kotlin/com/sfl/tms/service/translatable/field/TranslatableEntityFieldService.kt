package com.sfl.tms.service.translatable.field

import com.sfl.tms.domain.translatable.TranslatableEntityField
import com.sfl.tms.service.translatable.field.dto.TranslatableEntityFieldDto

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:11 PM
 */
interface TranslatableEntityFieldService {

    fun findByKeyAndEntity(key: String, uuid: String, label: String): TranslatableEntityField?

    fun getByKeyAndEntity(key: String, uuid: String, label: String): TranslatableEntityField

    fun create(dto: TranslatableEntityFieldDto): TranslatableEntityField
}