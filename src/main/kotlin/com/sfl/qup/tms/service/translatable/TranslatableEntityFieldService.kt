package com.sfl.qup.tms.service.translatable

import com.sfl.qup.tms.domain.translatable.TranslatableEntityField
import com.sfl.qup.tms.service.translatable.dto.field.TranslatableEntityFieldDto

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:11 PM
 */
interface TranslatableEntityFieldService {

    fun create(dto: TranslatableEntityFieldDto): TranslatableEntityField

    fun find(name: String, uuid:String): TranslatableEntityField?
}