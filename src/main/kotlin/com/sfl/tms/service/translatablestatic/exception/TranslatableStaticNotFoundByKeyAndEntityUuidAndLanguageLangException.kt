package com.sfl.tms.service.translatablestatic.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableStaticNotFoundByKeyAndEntityUuidAndLanguageLangException(key: String, uuid:String, lang: String) : RuntimeException("TranslatableStatic not found by $key key, $uuid entity uuid and $lang lang.")