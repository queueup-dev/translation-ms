package com.sfl.qup.tms.persistence.translatable

import com.sfl.qup.tms.domain.translatable.TranslatableEntityFieldTranslation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:09 PM
 */
@Repository
interface TranslatableEntityFieldTranslationRepository : JpaRepository<TranslatableEntityFieldTranslation, Long>