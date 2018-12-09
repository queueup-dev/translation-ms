package com.sfl.qup.tms.service.translatable.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableEntityTranslationExistException(lang: String, entityUuid: String) : RuntimeException("TranslatableEntityTranslation already exists by $lang lang and $entityUuid entity uuid.")