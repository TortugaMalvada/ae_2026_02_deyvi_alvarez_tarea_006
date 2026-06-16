package com.example.demo.controllers

import com.example.demo.dto.ProfessorRequest
import com.example.demo.dto.ProfessorResponse
import com.example.demo.services.ProfessorService
import org.springframework.web.bind.annotation.*

@RestController
class ProfessorController(
    private val professorService: ProfessorService
) {

    @PostMapping("/api/professors")
    fun createProfessor(
        @RequestBody request: ProfessorRequest
    ): ProfessorResponse {

        return professorService.createProfessor(request)
    }


    @GetMapping("/api/professors")
    fun getAllProfessors(): List<ProfessorResponse> {

        return professorService.getAllProfessors()
    }


    @GetMapping("/api/professors/{id}")
    fun getProfessorById(
        @PathVariable id: Long
    ): ProfessorResponse {

        return professorService.getProfessorById(id)
    }


    @PutMapping("/api/professors/{id}")
    fun updateProfessor(
        @PathVariable id: Long,
        @RequestBody request: ProfessorRequest
    ): ProfessorResponse {

        return professorService.updateProfessor(id, request)
    }


    @DeleteMapping("/api/professors/{id}")
    fun deleteProfessor(
        @PathVariable id: Long
    ) {

        professorService.deleteProfessor(id)
    }
}