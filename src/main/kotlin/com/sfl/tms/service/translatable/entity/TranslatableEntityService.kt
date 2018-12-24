package com.sfl.tms.service.translatable.entity

import com.sfl.tms.domain.translatable.TranslatableEntity
import com.sfl.tms.service.translatable.entity.dto.TranslatableEntityDto

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