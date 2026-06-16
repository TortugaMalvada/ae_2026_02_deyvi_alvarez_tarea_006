package com.example.demo.mappers

import com.example.demo.dto.EnrollmentResponse
import com.example.demo.entities.Enrollment
import java.time.LocalDateTime

fun Enrollment.toResponse(): EnrollmentResponse {
    return EnrollmentResponse(
        id = this.id,
        createdAt = LocalDateTime.now(), // Asigna la fecha actual para el JSON de respuesta
        status = this.status,
        student = this.student.toResponse(), // Transforma el estudiante completo a DTO
        subject = this.subject.toResponse()   // Transforma la materia completa a DTO
    )
}