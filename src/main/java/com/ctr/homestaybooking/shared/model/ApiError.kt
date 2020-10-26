package com.ctr.homestaybooking.shared.model

import org.hibernate.validator.internal.engine.path.PathImpl
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import java.time.ZonedDateTime
import java.util.*
import java.util.function.Consumer
import javax.validation.ConstraintViolation

data class ApiError(var status: HttpStatus = HttpStatus.OK,
                    var timestamp: ZonedDateTime = ZonedDateTime.now(),
                    var message: String? = null,
                    var debugMessage: String? = null) {

    private var subErrors: MutableList<ApiSubError>? = null

    constructor(status: HttpStatus) : this() {
        this.status = status
    }

    constructor(status: HttpStatus, ex: Throwable) : this() {
        this.status = status
        message = "Unexpected error"
        debugMessage = ex.localizedMessage
    }

    constructor(status: HttpStatus, message: String?, ex: Throwable) : this() {
        this.status = status
        this.message = message
        debugMessage = ex.localizedMessage
    }

    private fun addSubError(subError: ApiSubError) {
        if (subErrors == null) {
            subErrors = ArrayList()
        }
        subErrors!!.add(subError)
    }

    private fun addValidationError(`object`: String, field: String, rejectedValue: Any?, message: String?) {
        addSubError(ApiValidationError(`object`, field, rejectedValue, message))
    }

    private fun addValidationError(`object`: String, message: String?) {
        addSubError(ApiValidationError(`object`, message))
    }

    private fun addValidationError(fieldError: FieldError) {
        this.addValidationError(
                fieldError.objectName,
                fieldError.field,
                fieldError.rejectedValue,
                fieldError.defaultMessage)
    }

    fun addValidationErrors(fieldErrors: List<FieldError>) {
        fieldErrors.forEach(Consumer { fieldError: FieldError -> this.addValidationError(fieldError) })
    }

    private fun addValidationError(objectError: ObjectError) {
        this.addValidationError(
                objectError.objectName,
                objectError.defaultMessage)
    }

    fun addValidationError(globalErrors: List<ObjectError>) {
        globalErrors.forEach(Consumer { objectError: ObjectError -> this.addValidationError(objectError) })
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     *
     * @param cv the ConstraintViolation
     */
    private fun addValidationError(cv: ConstraintViolation<*>) {
        this.addValidationError(
                cv.rootBeanClass.simpleName,
                (cv.propertyPath as PathImpl).leafNode.asString(),
                cv.invalidValue,
                cv.message)
    }

    fun addValidationErrors(constraintViolations: Set<ConstraintViolation<*>>) {
        constraintViolations.forEach(Consumer { cv: ConstraintViolation<*> -> this.addValidationError(cv) })
    }

    fun getSubErrors(): List<ApiSubError>? {
        return subErrors
    }

    fun setSubErrors(subErrors: MutableList<ApiSubError>?) {
        this.subErrors = subErrors
    }
}


abstract class ApiSubError
internal data class ApiValidationError(
        var `object`: String,
        var field: String? = null,
        var rejectedValue: Any? = null,
        var message: String? = null) : ApiSubError()
