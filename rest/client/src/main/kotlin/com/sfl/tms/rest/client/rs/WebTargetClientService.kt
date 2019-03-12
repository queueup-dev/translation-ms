package com.sfl.tms.rest.client.rs

import javax.ws.rs.client.WebTarget

/**
 * User: Vazgen Danielyan
 * Date: 2/11/19
 * Time: 11:23 PM
 */
interface WebTargetClientService : TranslationClientService {
    val languageWebTarget: WebTarget

    val translationWebTarget: WebTarget
}