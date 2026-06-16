package com.example.demo.mappers

import com.example.demo.dto.ProfessorRequest
import com.example.demo.dto.ProfessorResponse
import com.example.demo.entities.Professor

fun ProfessorRequest.toEntity(): Professor {
    return Professor(
        name = name,
        email = email
    )
}

fun Professor.toResponse(): ProfessorResponse {
    return ProfessorResponse(
        id = id,
        name = name,
        email = email
    )
}