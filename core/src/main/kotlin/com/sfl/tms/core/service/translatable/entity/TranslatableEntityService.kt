package com.sfl.tms.core.service.translatable.entity

import com.sfl.tms.core.domain.translatable.TranslatableEntity
import com.sfl.tms.core.service.translatable.entity.dto.TranslatableEntityDto

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:11 PM
 */
interface TranslatableEntityService {

    fun findAll(): List<TranslatableEntity>

    fun findByUuidAndLabel(uuid: String, label: String): TranslatableEntity?

    fun findByUuid(uuid: String): List<TranslatableEntity>

    fun getByUuidAndLabel(uuid: String, label: String): TranslatableEntity

    fun create(dto: TranslatableEntityDto): TranslatableEntity

}