package com.sfl.tms.service.translatablestatic.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableStaticNotFoundByKeyAndEntityUuidException(key: String, uuid:String) : RuntimeException("TranslatableStatic not found by $key key and entity $uuid uuid.")