package com.sfl.qup.tms.service.translatable

import com.sfl.qup.tms.domain.translatable.TranslatableEntity
import com.sfl.qup.tms.service.translatable.dto.entity.TranslatableEntityDto

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:11 PM
 */
interface TranslatableEntityService {

    fun findByUuid(uuid: String): TranslatableEntity?

    fun getByUuid(uuid: String): TranslatableEntity

    fun create(dto: TranslatableEntityDto): TranslatableEntity

}