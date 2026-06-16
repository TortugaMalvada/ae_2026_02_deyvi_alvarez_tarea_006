package com.example.demo.controllers

import com.example.demo.dto.SubjectRequest
import com.example.demo.dto.SubjectResponse
import com.example.demo.services.SubjectService
import org.springframework.web.bind.annotation.*

@RestController
class SubjectController(
    private val subjectService: SubjectService
) {

    @PostMapping("/api/subjects")
    fun createSubject(
        @RequestBody request: SubjectRequest
    ): SubjectResponse {

        return subjectService.createSubject(request)
    }


    @GetMapping("/api/subjects")
    fun getAllSubjects(): List<SubjectResponse> {

        return subjectService.getAllSubjects()
    }


    @GetMapping("/api/subjects/{id}")
    fun getSubjectById(
        @PathVariable id: Long
    ): SubjectResponse {

        return subjectService.getSubjectById(id)
    }


    @PutMapping("/api/subjects/{id}")
    fun updateSubject(
        @PathVariable id: Long,
        @RequestBody request: SubjectRequest
    ): SubjectResponse {

        return subjectService.updateSubject(id, request)
    }


    @DeleteMapping("/api/subjects/{id}")
    fun deleteSubject(
        @PathVariable id: Long
    ) {

        subjectService.deleteSubject(id)
    }
}