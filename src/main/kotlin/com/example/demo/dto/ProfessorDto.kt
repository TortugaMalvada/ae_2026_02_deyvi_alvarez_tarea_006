package com.example.demo.dto

data class ProfessorRequest(
    val name: String = "",
    val email: String = ""
)

data class ProfessorResponse(
    val id: Long = 0L,
    val name: String = "",
    val email: String = ""
)