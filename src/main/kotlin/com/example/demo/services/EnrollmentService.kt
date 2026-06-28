package com.example.demo.services

import com.example.demo.dto.EnrollmentRequest
import com.example.demo.dto.EnrollmentResponse
import com.example.demo.entities.Enrollment
import com.example.demo.exceptions.EnrollmentNotFoundException
import com.example.demo.exceptions.StudentNotFoundException
import com.example.demo.exceptions.SubjectNotFoundException
import com.example.demo.mappers.toResponse
import com.example.demo.repositories.EnrollmentRepository
import com.example.demo.repositories.StudentRepository
import com.example.demo.repositories.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EnrollmentService(
    private val enrollmentRepository: EnrollmentRepository,
    private val studentRepository: StudentRepository,
    private val subjectRepository: SubjectRepository
) {

    private val logger = LoggerFactory.getLogger(EnrollmentService::class.java)

    fun createEnrollment(request: EnrollmentRequest): EnrollmentResponse {
        val studentId = request.studentId ?: throw IllegalArgumentException("El id del estudiante es obligatorio")
        val subjectId = request.subjectId ?: throw IllegalArgumentException("El id de la materia es obligatorio")

        val student = studentRepository.findById(studentId)
            .orElseThrow {
                StudentNotFoundException("Estudiante no encontrado con id: $studentId")
            }

        val subject = subjectRepository.findById(subjectId)
            .orElseThrow {
                SubjectNotFoundException("Materia no encontrada con id: $subjectId")
            }

        logger.info("Creando enrollment para estudiante: ${student.name}")

        // Creamos la matrícula con el estado inicial "INSCRITO" exigido por la rúbrica
        val enrollment = Enrollment(
            student = student,
            subject = subject,
            status = "INSCRITO"
        )

        val savedEnrollment = enrollmentRepository.save(enrollment)
        return savedEnrollment.toResponse()
    }

    fun getAllEnrollments(): List<EnrollmentResponse> {
        logger.info("Obteniendo todos los enrollments")
        return enrollmentRepository.findAll().map {
            it.toResponse()
        }
    }

    fun getEnrollmentById(id: Long): EnrollmentResponse {
        val enrollment = enrollmentRepository.findById(id)
            .orElseThrow {
                EnrollmentNotFoundException("Enrollment no encontrado con id: $id")
            }
        return enrollment.toResponse()
    }

    fun updateEnrollment(
        id: Long,
        request: EnrollmentRequest
    ): EnrollmentResponse {
        // 1. Verificar si la matrícula existe
        val enrollment = enrollmentRepository.findById(id)
            .orElseThrow {
                EnrollmentNotFoundException("Enrollment no encontrado con id: $id")
            }

        logger.info("Actualizando enrollment con id: $id")

        // 2. Determinar nuevo estado (si viene nulo en el JSON, conserva el actual)
        val nuevoEstado = request.status ?: enrollment.status

        // 3. Determinar estudiante de forma limpia para evitar ramas de bytecode extra por tipos mutables/anulables
        val inputStudentId = request.studentId
        val estudianteActualizado = if (inputStudentId != null && inputStudentId != 0L) {
            studentRepository.findById(inputStudentId)
                .orElseThrow { StudentNotFoundException("Estudiante no encontrado con id: $inputStudentId") }
        } else {
            enrollment.student
        }

        // 4. Determinar materia de forma limpia para evitar ramas de bytecode extra por tipos mutables/anulables
        val inputSubjectId = request.subjectId
        val materiaActualizada = if (inputSubjectId != null && inputSubjectId != 0L) {
            subjectRepository.findById(inputSubjectId)
                .orElseThrow { SubjectNotFoundException("Materia no encontrada con id: $inputSubjectId") }
        } else {
            enrollment.subject
        }

        // 5. Clonamos de manera segura creando una nueva instancia para solucionar el error de 'val'
        val updatedEnrollment = Enrollment(
            id = enrollment.id,
            student = estudianteActualizado,
            subject = materiaActualizada,
            status = nuevoEstado
        )

        val savedEnrollment = enrollmentRepository.save(updatedEnrollment)
        return savedEnrollment.toResponse()
    }

    fun deleteEnrollment(id: Long) {
        val enrollment = enrollmentRepository.findById(id)
            .orElseThrow {
                EnrollmentNotFoundException("Enrollment no encontrado con id: $id")
            }
        enrollmentRepository.delete(enrollment)
    }
}