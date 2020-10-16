package com.ctr.homestaybooking.shared.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiData<T>(var body: T,
                      var length: Int = 1,
                      var message: String? = null) {

    init {
        if (this.body is List<*>) {
            length = (this.body as List<*>).size
        }
        if (this.body is Map<*, *>) {
            length = (this.body as Map<*, *>).size
        }
    }
}
