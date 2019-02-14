package com.sfl.tms.rest.client.rs.model

import com.sfl.tms.rest.common.model.AbstractApiModel
import javax.ws.rs.core.GenericType

/**
 * User: Vazgen Danielyan
 * Date: 2/12/19
 * Time: 12:22 AM
 */
class ResponseGenericType<T : AbstractApiModel> : GenericType<T>()