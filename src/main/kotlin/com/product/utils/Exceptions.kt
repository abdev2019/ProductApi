package com.product.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.TransactionSystemException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*
import java.util.stream.Collectors
import javax.validation.ConstraintViolationException


@ResponseStatus(value= HttpStatus.NOT_FOUND)
class ProductNotFoundException() : RuntimeException("Product not found") {}

@ResponseStatus(value= HttpStatus.NOT_FOUND)
class RatingNotFoundException() : RuntimeException("Rating not exist !") {}

@ResponseStatus(value= HttpStatus.NOT_FOUND)
class ImageNotFoundException() : RuntimeException("Image not found") {}

@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE)
class OnlyImagesAllowedException() : RuntimeException("Only images allowed !") {}


// For data validation
@ControllerAdvice
class ValidationExceptionHandler @Autowired constructor(private val messageSource: MessageSource):
        ResponseEntityExceptionHandler() {

    // handled when no controller validation activated;
    // When hibernate validation process executed, Mostly when updating entities
    @ExceptionHandler(TransactionSystemException::class)
    protected fun handlePersistenceException(ex: Exception, request: WebRequest?): ResponseEntity<Any?>? {
        print("TransactionSystemException handled !")
        val body: MutableMap<String, Any?> = HashMap()
        val cause = (ex as TransactionSystemException).rootCause
        if (cause is ConstraintViolationException) {
            val errors: MutableList<String> = ArrayList()
            for (violation in cause.constraintViolations)
                errors.add(violation.propertyPath.toString() + ": " + violation.message)
            body["status"] = HttpStatus.BAD_REQUEST
            body["message"] = "Data validation failed !"
            body["errors"] = errors
            body["timestamp"] = Date()

            return ResponseEntity.badRequest().body(body)
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred")
    }

    // handled when controller validation activated (@Valid annotation)
    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any>
    {
        print("MethodArgumentNotValidException handled !")
        val errors = ex.bindingResult.fieldErrors.stream().map { x: FieldError -> x.field+" : "+x.defaultMessage }.collect(Collectors.toList())
        val body: MutableMap<String, Any> = LinkedHashMap()
        body["status"] = status.value()
        body["message"] = "Data validation failed !"
        body["errors"] = errors
        body["timestamp"] = Date()
        return ResponseEntity(body, headers, status)
    }
}