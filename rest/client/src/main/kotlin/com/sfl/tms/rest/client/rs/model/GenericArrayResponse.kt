package com.sfl.tms.rest.client.rs.model

import com.sfl.tms.rest.common.model.response.AbstractApiResponseModel

/**
 * User: Vazgen Danielyan
 * Date: 2/12/19
 * Time: 12:22 AM
 */
class GenericArrayResponse<T> : AbstractApiResponseModel, ArrayList<T>()