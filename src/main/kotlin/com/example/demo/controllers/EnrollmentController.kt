package com.example.demo.controllers

import com.example.demo.dto.EnrollmentRequest
import com.example.demo.dto.EnrollmentResponse
import com.example.demo.services.EnrollmentService
import org.springframework.web.bind.annotation.*

@RestController
class EnrollmentController(
    private val enrollmentService: EnrollmentService
) {

    @PostMapping("/api/enrollments")
    fun createEnrollment(
        @RequestBody request: EnrollmentRequest
    ): EnrollmentResponse {

        return enrollmentService.createEnrollment(request)
    }


    @GetMapping("/api/enrollments")
    fun getAllEnrollments(): List<EnrollmentResponse> {

        return enrollmentService.getAllEnrollments()
    }


    @GetMapping("/api/enrollments/{id}")
    fun getEnrollmentById(
        @PathVariable id: Long
    ): EnrollmentResponse {

        return enrollmentService.getEnrollmentById(id)
    }


    @PutMapping("/api/enrollments/{id}")
    fun updateEnrollment(
        @PathVariable id: Long,
        @RequestBody request: EnrollmentRequest
    ): EnrollmentResponse {

        return enrollmentService.updateEnrollment(id, request)
    }


    @DeleteMapping("/api/enrollments/{id}")
    fun deleteEnrollment(
        @PathVariable id: Long
    ) {

        enrollmentService.deleteEnrollment(id)
    }
}