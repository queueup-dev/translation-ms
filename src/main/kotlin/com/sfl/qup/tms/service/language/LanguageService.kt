package com.sfl.qup.tms.service.language

import com.sfl.qup.tms.domain.language.Language

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:11 PM
 */
interface LanguageService {

    fun findByLang(lang: String): Language?

    fun getByLang(lang: String): Language

    fun getAll(): List<Language>

    fun get(id: Long): Language

    fun create(lang: String): Language
}