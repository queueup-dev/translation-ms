package com.sfl.qup.tms.service.translatable.entity.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableEntityNotFoundByUuidException(val uuid: String) : RuntimeException("Translatable entity not found by $uuid uuid.")