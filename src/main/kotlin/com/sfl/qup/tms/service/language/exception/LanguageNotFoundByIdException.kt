package com.sfl.qup.tms.service.language.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:23 PM
 */
class LanguageNotFoundByIdException(id: Long) : RuntimeException("Language not found by $id id.")