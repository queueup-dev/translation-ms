package com.sfl.tms.core.persistence.translatable

import com.sfl.tms.core.domain.translatable.TranslatableEntity
import com.sfl.tms.core.domain.translatable.TranslatableEntityField
import com.sfl.tms.core.domain.translatable.TranslatableEntityFieldType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:09 PM
 */
@Repository
interface TranslatableEntityFieldRepository : JpaRepository<TranslatableEntityField, Long> {
    fun findByKeyAndTypeAndEntity(key: String, type: TranslatableEntityFieldType, entity: TranslatableEntity): TranslatableEntityField?
}