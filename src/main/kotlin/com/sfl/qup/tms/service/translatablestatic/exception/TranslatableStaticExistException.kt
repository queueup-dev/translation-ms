package com.sfl.qup.tms.service.translatablestatic.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableStaticExistException(key: String, languageId: Long) : RuntimeException("TranslatableStatic already exists by $key key and $languageId languageId.")