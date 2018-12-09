package com.sfl.qup.tms.service.language.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:23 PM
 */
class LanguageExistByLangException(lang: String) : RuntimeException("Language exist by $lang lang.")