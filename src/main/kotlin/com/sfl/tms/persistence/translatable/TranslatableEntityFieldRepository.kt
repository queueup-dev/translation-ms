package com.sfl.tms.persistence.translatable

import com.sfl.tms.domain.translatable.TranslatableEntityField
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:09 PM
 */
@Repository
interface TranslatableEntityFieldRepository : JpaRepository<TranslatableEntityField, Long> {
    fun findByNameAndEntity_Uuid(name: String, uuid: String): TranslatableEntityField?
}