package com.sfl.qup.tms.service.translatablestatics.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableStaticNotFoundByKeyAndLanguageIdException(key: String, languageId: Long) : RuntimeException("TranslatableStatic not found by $key key and $languageId languageId.")