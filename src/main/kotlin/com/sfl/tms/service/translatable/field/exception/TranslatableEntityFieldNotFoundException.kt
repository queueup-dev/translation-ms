package com.sfl.tms.service.translatable.field.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableEntityFieldNotFoundException(msg: String) : RuntimeException(msg) {
    constructor(key: String, uuid: String) : this("TranslatableEntityField not found by '$key' key and '$uuid' uuid.")
    constructor(key: String, uuid: String, label: String) : this("TranslatableEntityField not found by '$key' key, '$uuid' uuid and '$label' label.")
}