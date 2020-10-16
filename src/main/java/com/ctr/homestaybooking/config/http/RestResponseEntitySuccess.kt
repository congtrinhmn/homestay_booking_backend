package com.ctr.homestaybooking.config.http

import com.ctr.homestaybooking.shared.model.ApiData
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
class RestResponseEntitySuccess : ResponseBodyAdvice<Any> {
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(o: Any?, methodParameter: MethodParameter, mediaType: MediaType, aClass: Class<out HttpMessageConverter<*>>, serverHttpRequest: ServerHttpRequest, serverHttpResponse: ServerHttpResponse): Any? {
        if (methodParameter.containingClass.isAnnotationPresent(RestController::class.java)) {
            if (!methodParameter.method!!.isAnnotationPresent(IgnoreResponseBinding::class.java)) {
                if (o !is ApiData<*>) {
                    return ApiData(o)
                }
            }
        }
        return o
    }
}
