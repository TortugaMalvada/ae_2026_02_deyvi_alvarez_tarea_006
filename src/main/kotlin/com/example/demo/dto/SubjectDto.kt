package com.example.demo.dto

data class SubjectRequest(
    val name: String = "",
    val professorId: Long = 0L
)

data class SubjectResponse(
    val id: Long = 0L,
    val name: String = "",
    val professorName: String = ""
)