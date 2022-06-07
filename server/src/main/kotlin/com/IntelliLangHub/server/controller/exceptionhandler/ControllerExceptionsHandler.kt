package com.intellilanghub.server.controller.exceptionhandler

import com.intellilanghub.server.exception.CommitNotActiveException
import com.intellilanghub.server.exception.EntityNotFoundException
import com.intellilanghub.server.exception.InvalidLanguageConfigurationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerExceptionsHandler {
    @ExceptionHandler(EntityNotFoundException::class)
    fun entityNotFoundException(e: EntityNotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)

    @ExceptionHandler(CommitNotActiveException::class)
    fun commitNotActiveException(e: CommitNotActiveException) =
        ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)

    @ExceptionHandler(InvalidLanguageConfigurationException::class)
    fun invalidLanguageConfigurationException(e: InvalidLanguageConfigurationException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
}