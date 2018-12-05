package com.sfl.qup.tms.service.language

import com.sfl.qup.tms.domain.language.Language

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:11 PM
 */
interface LanguageService {

    fun get(lang: String): Language

    fun find(lang: String): Language?

    fun create(lang: String): Language
}