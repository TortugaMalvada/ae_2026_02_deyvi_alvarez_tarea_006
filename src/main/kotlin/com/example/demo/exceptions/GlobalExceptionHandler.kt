package com.example.demo.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [EmailAlreadyExistsException::class])
    fun handleEmailAlreadyExistsException(e: EmailAlreadyExistsException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            message = "El Email ya existe: ${e.message}",
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse)
    }
    @ExceptionHandler(value = [StudentNotFoundException::class])
    fun handleStudentNotFoundException(
        e: StudentNotFoundException
    ): ResponseEntity<ErrorResponse> {

        val errorResponse = ErrorResponse(
            message = e.message ?: "Recurso no encontrado",
            timestamp = LocalDateTime.now()
        )

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorResponse)
    }
    @ExceptionHandler(value = [BlankNameException::class])
    fun handleBlankNameException(
        e: BlankNameException
    ): ResponseEntity<ErrorResponse> {

        val errorResponse = ErrorResponse(
            message = e.message ?: "El nombre no puede estar vacío",
            timestamp = LocalDateTime.now()
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse)
    }
    @ExceptionHandler(value = [ProfessorNotFoundException::class])
    fun handleProfessorNotFoundException(
        e: ProfessorNotFoundException
    ): ResponseEntity<ErrorResponse> {

        val errorResponse = ErrorResponse(
            message = e.message ?: "Profesor no encontrado",
            timestamp = LocalDateTime.now()
        )

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorResponse)
    }
    @ExceptionHandler(value = [SubjectNotFoundException::class])
    fun handleSubjectNotFoundException(
        e: SubjectNotFoundException
    ): ResponseEntity<ErrorResponse> {

        val errorResponse = ErrorResponse(
            message = e.message ?: "Materia no encontrada",
            timestamp = LocalDateTime.now()
        )

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorResponse)
    }
    @ExceptionHandler(value = [EnrollmentNotFoundException::class])
    fun handleEnrollmentNotFoundException(
        e: EnrollmentNotFoundException
    ): ResponseEntity<ErrorResponse> {

        val errorResponse = ErrorResponse(
            message = e.message ?: "Enrollment no encontrado",
            timestamp = LocalDateTime.now()
        )

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorResponse)
    }
}

data class ErrorResponse(
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)