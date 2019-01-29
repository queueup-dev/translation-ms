package com.sfl.tms.rest.client.language

import com.sfl.tms.rest.common.communicator.language.LanguageCommunicator
import org.springframework.cloud.openfeign.FeignClient

/**
 * User: Vazgen Danielyan
 * Date: 1/26/19
 * Time: 11:05 PM
 */
@FeignClient(name = "LanguageApiClient")
interface LanguageApiClient : LanguageCommunicator