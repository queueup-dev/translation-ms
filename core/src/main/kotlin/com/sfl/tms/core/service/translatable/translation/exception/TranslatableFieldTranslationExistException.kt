package com.sfl.tms.core.service.translatable.translation.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableFieldTranslationExistException(msg: String) : RuntimeException(msg) {
    constructor(key: String, uuid: String, label: String, lang: String) : this("TranslatableEntityFieldTranslation exists for '$key' key, '$uuid' uuid, '$label' label and '$lang' lang.")
}