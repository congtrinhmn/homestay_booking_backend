package com.ctr.homestaybooking.shared.model

import org.hibernate.validator.internal.engine.path.PathImpl
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import java.time.ZonedDateTime
import java.util.*
import java.util.function.Consumer
import javax.validation.ConstraintViolation

class ApiError(var status: HttpStatus = HttpStatus.OK,
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

    override fun equals(o: Any?): Boolean {
        if (o === this) return true
        if (o !is ApiError) return false
        val other = o
        if (!other.canEqual(this as Any)) return false
        val `this$status`: Any? = status
        val `other$status`: Any? = other.status
        if (if (`this$status` == null) `other$status` != null else `this$status` != `other$status`) return false
        val `this$timestamp`: Any = timestamp
        val `other$timestamp`: Any = other.timestamp
        if (if (`this$timestamp` == null) `other$timestamp` != null else `this$timestamp` != `other$timestamp`) return false
        val `this$message`: Any? = message
        val `other$message`: Any? = other.message
        if (if (`this$message` == null) `other$message` != null else `this$message` != `other$message`) return false
        val `this$debugMessage`: Any? = debugMessage
        val `other$debugMessage`: Any? = other.debugMessage
        if (if (`this$debugMessage` == null) `other$debugMessage` != null else `this$debugMessage` != `other$debugMessage`) return false
        val `this$subErrors`: Any? = getSubErrors()
        val `other$subErrors`: Any? = other.getSubErrors()
        return if (if (`this$subErrors` == null) `other$subErrors` != null else `this$subErrors` != `other$subErrors`) false else true
    }

    protected fun canEqual(other: Any?): Boolean {
        return other is ApiError
    }

    override fun hashCode(): Int {
        val PRIME = 59
        var result = 1
        val `$status`: Any? = status
        result = result * PRIME + (`$status`?.hashCode() ?: 43)
        val `$timestamp`: Any = timestamp
        result = result * PRIME + (`$timestamp`?.hashCode() ?: 43)
        val `$message`: Any? = message
        result = result * PRIME + (`$message`?.hashCode() ?: 43)
        val `$debugMessage`: Any? = debugMessage
        result = result * PRIME + (`$debugMessage`?.hashCode() ?: 43)
        val `$subErrors`: Any? = getSubErrors()
        result = result * PRIME + (`$subErrors`?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String {
        return "ApiError(status=" + status + ", timestamp=" + timestamp + ", message=" + message + ", debugMessage=" + debugMessage + ", subErrors=" + getSubErrors() + ")"
    }

    abstract inner class ApiSubError
    internal inner class ApiValidationError : ApiSubError {
        var `object`: String
        var field: String? = null
        var rejectedValue: Any? = null
        var message: String?

        constructor(`object`: String, message: String?) {
            this.`object` = `object`
            this.message = message
        }

        constructor(`object`: String, field: String?, rejectedValue: Any?, message: String?) {
            this.`object` = `object`
            this.field = field
            this.rejectedValue = rejectedValue
            this.message = message
        }

        override fun toString(): String {
            return "ApiError.ApiValidationError(object=" + `object` + ", field=" + field + ", rejectedValue=" + rejectedValue + ", message=" + this.message + ")"
        }

        override fun equals(o: Any?): Boolean {
            if (o === this) return true
            if (o !is ApiValidationError) return false
            val other = o
            if (!other.canEqual(this as Any)) return false
            val `this$object`: Any = `object`
            val `other$object`: Any = other.`object`
            if (if (`this$object` == null) `other$object` != null else `this$object` != `other$object`) return false
            val `this$field`: Any? = field
            val `other$field`: Any? = other.field
            if (if (`this$field` == null) `other$field` != null else `this$field` != `other$field`) return false
            val `this$rejectedValue` = rejectedValue
            val `other$rejectedValue` = other.rejectedValue
            if (if (`this$rejectedValue` == null) `other$rejectedValue` != null else `this$rejectedValue` != `other$rejectedValue`) return false
            val `this$message`: Any? = this.message
            val `other$message`: Any? = other.message
            return if (if (`this$message` == null) `other$message` != null else `this$message` != `other$message`) false else true
        }

        protected fun canEqual(other: Any?): Boolean {
            return other is ApiValidationError
        }

        override fun hashCode(): Int {
            val PRIME = 59
            var result = 1
            val `$object`: Any = `object`
            result = result * PRIME + (`$object`?.hashCode() ?: 43)
            val `$field`: Any? = field
            result = result * PRIME + (`$field`?.hashCode() ?: 43)
            val `$rejectedValue` = rejectedValue
            result = result * PRIME + (`$rejectedValue`?.hashCode() ?: 43)
            val `$message`: Any? = this.message
            result = result * PRIME + (`$message`?.hashCode() ?: 43)
            return result
        }
    }

}
