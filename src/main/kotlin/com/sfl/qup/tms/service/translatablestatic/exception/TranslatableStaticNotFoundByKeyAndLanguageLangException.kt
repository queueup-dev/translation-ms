package com.sfl.qup.tms.service.translatablestatic.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableStaticNotFoundByKeyAndLanguageLangException(key: String, lang: String) : RuntimeException("TranslatableStatic not found by $key key and $lang lang.")