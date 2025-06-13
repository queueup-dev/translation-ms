package com.sfl.tms.rest.client.rs

import jakarta.ws.rs.client.Client

/**
 * User: Vazgen Danielyan
 * Date: 2/11/19
 * Time: 11:22 PM
 */
interface TranslationClientService {
    fun getClient(): Client
}