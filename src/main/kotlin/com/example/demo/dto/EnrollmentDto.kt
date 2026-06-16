package com.example.demo.dto

import java.time.LocalDateTime

data class EnrollmentRequest(
    val studentId: Long? = null,
    val subjectId: Long? = null,
    val status: String? = null
)

data class EnrollmentResponse(
    val id: Long,
    val createdAt: LocalDateTime,
    val status: String,
    val student: StudentResponse,
    val subject: SubjectResponse
)