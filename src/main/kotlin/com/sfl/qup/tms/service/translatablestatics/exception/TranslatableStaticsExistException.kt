package com.sfl.qup.tms.service.translatablestatics.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableStaticsExistException(key: String, lang: String) : RuntimeException("TranslatableStatic already exists by $key key and $lang lang.")