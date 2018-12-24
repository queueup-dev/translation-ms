package com.sfl.tms.service.translatablestatic.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableStaticExistException(key: String, lang: String) : RuntimeException("TranslatableStatic already exists by $key key and $lang language.")