package com.sfl.tms.core.persistence.language

import com.sfl.tms.core.domain.language.Language
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:08 PM
 */
@Repository
interface LanguageRepository : JpaRepository<Language, Long> {
    fun findByLang(lang: String): Language?
}