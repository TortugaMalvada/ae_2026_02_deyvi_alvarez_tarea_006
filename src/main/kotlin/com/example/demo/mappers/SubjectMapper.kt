package com.example.demo.mappers

import com.example.demo.dto.SubjectResponse
import com.example.demo.entities.Subject

fun Subject.toResponse(): SubjectResponse {
    return SubjectResponse(
        id = id,
        name = name,
        professorName = professor.name
    )
}