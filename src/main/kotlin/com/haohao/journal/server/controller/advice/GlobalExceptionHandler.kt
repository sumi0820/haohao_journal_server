package com.haohao.journal.server.controller.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                error = HttpStatus.BAD_REQUEST.reasonPhrase,
                message = ex.message ?: "Bad Request",
            )
        return ResponseEntity.badRequest().body(errorResponse)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                error = HttpStatus.BAD_REQUEST.reasonPhrase,
                message = ex.message ?: "Bad Request",
            )
        return ResponseEntity.badRequest().body(errorResponse)
    }

    data class ErrorResponse(
        val status: Int,
        val error: String,
        val message: String,
    )
}
