package com.intellilanghub.server.controller.exceptionhandler

import exception.CommitNotActiveException
import exception.EntityNotFoundException
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
}