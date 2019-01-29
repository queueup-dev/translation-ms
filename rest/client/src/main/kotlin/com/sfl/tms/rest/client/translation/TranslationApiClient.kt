package com.sfl.tms.rest.client.translation

import com.sfl.tms.rest.common.communicator.translation.TranslationCommunicator
import org.springframework.cloud.openfeign.FeignClient

/**
 * User: Vazgen Danielyan
 * Date: 1/27/19
 * Time: 11:49 PM
 */
@FeignClient(name = "TranslationApiClient", url = "http://localhost:8080/")
interface TranslationApiClient : TranslationCommunicator