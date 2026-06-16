package com.example.demo.mappers

import com.example.demo.dto.StudentRequest
import com.example.demo.dto.StudentResponse
import com.example.demo.entities.Student

fun StudentRequest.toEntity(): Student {
    return Student(
        name = name,
        email = email
    )
}

fun Student.toResponse(): StudentResponse {
    return StudentResponse(
        id = id,
        name = name,
        email = email
    )
}