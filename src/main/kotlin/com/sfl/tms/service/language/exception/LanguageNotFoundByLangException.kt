package com.sfl.tms.service.language.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:23 PM
 */
class LanguageNotFoundByLangException(lang: String) : RuntimeException("Language not found by $lang lang.")