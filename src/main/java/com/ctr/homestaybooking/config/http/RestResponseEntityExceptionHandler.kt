package com.ctr.homestaybooking.config.http

import com.ctr.homestaybooking.shared.log
import com.ctr.homestaybooking.shared.model.ApiError
import com.ctr.homestaybooking.shared.model.Response
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.nio.file.AccessDeniedException
import java.util.function.Consumer
import javax.persistence.EntityNotFoundException

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    override fun handleHttpMediaTypeNotSupported(
            ex: HttpMediaTypeNotSupportedException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest): ResponseEntity<Any> {
        val builder = StringBuilder()
        builder.append(ex.contentType)
        builder.append(" media type is not supported. Supported media types are ")
        ex.supportedMediaTypes.forEach(Consumer { t: MediaType? -> builder.append(t).append(", ") })
        return Response.buildResponseError(ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length - 2), ex))
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    override fun handleMethodArgumentNotValid(
            ex: MethodArgumentNotValidException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest): ResponseEntity<Any> {
        val apiError = ApiError(HttpStatus.BAD_REQUEST)
        apiError.message = "Validation error"
        apiError.addValidationErrors(ex.bindingResult.fieldErrors)
        apiError.addValidationError(ex.bindingResult.globalErrors)
        return Response.buildResponseError(apiError)
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val servletWebRequest = request as ServletWebRequest
        log.info { "${servletWebRequest.httpMethod} to ${servletWebRequest.request.servletPath}" }
        val error = "Malformed JSON request"
        return Response.buildResponseError(ApiError(HttpStatus.BAD_REQUEST, error, ex))
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex the Exception
     * @return the ApiError object
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    protected fun handleMethodArgumentTypeMismatch(ex: MethodArgumentTypeMismatchException,
                                                   request: WebRequest?): ResponseEntity<Any> {
        val apiError = ApiError(HttpStatus.BAD_REQUEST)
        apiError.message = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.name, ex.value, ex.requiredType!!.simpleName)
        apiError.debugMessage = ex.message
        return Response.buildResponseError(apiError)
    }

    /**
     * Handle [javax.persistence.EntityNotFoundException]
     */
    @ExceptionHandler(EntityNotFoundException::class)
    protected fun handleEntityNotFound(ex: EntityNotFoundException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.NOT_FOUND, ex))
    }

    /**
     * Handle [IllegalArgumentException]
     */
    @ExceptionHandler(value = [IllegalArgumentException::class])
    protected fun handleIllegalArgumentConflict(ex: RuntimeException, request: WebRequest?): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.BAD_REQUEST, ex))
    }

    /**
     * Handle [AccessDeniedException]
     */
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
            ex: Exception?, request: WebRequest?): ResponseEntity<Any> {
        return ResponseEntity(
                "Access denied message here", HttpHeaders(), HttpStatus.FORBIDDEN)
    }
}
