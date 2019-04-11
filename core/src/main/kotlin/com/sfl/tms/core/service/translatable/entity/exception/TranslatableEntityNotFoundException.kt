package com.sfl.tms.core.service.translatable.entity.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableEntityNotFoundException : RuntimeException {

    constructor(uuid: String) : super("Translatable entity not found by '$uuid' uuid.")

    constructor(uuid: String, label: String) : super("Translatable entity not found by '$uuid' uuid and '$label' label.")
}