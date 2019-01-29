package com.sfl.tms.core.persistence.translatable

import com.sfl.tms.core.domain.translatable.TranslatableEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:09 PM
 */
@Repository
interface TranslatableEntityRepository : JpaRepository<TranslatableEntity, Long> {

    fun findByUuidAndLabel(uuid: String, label: String): TranslatableEntity?

}