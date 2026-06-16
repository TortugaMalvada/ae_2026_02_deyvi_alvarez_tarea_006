package com.example.demo.controllers

import com.example.demo.dto.StudentRequest
import com.example.demo.dto.StudentResponse
import com.example.demo.services.StudentService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
class StudentController(
    val studentService: StudentService
) {

    private val logger = LoggerFactory.getLogger(StudentController::class.java)

    @PostMapping(value = ["/api/students"])
    fun createStudent(
        @RequestBody request: StudentRequest
    ): StudentResponse {

        logger.info("Creando estudiante ${request.name}")

        return studentService.createStudent(request)
    }


    @GetMapping(value = ["/api/students"])
    fun getAllStudents(): List<StudentResponse> {

        logger.info("Tomando a todos los estudiantes")

        return studentService.getAllStudents()
    }


    @GetMapping("/api/students/{id}")
    fun getStudentById(
        @PathVariable id: Long
    ): StudentResponse {

        return studentService.getStudentById(id)
    }


    @PutMapping("/api/students/{id}")
    fun updateStudent(
        @PathVariable id: Long,
        @RequestBody request: StudentRequest
    ): StudentResponse {

        logger.info("Actualizando estudiante con id: $id")

        return studentService.updateStudent(id, request)
    }


    @DeleteMapping("/api/students/{id}")
    fun deleteStudent(
        @PathVariable id: Long
    ) {

        logger.info("Eliminando estudiante con id: $id")

        studentService.deleteStudent(id)
    }
}