package com.example.demo.services

import com.example.demo.dto.SubjectRequest
import com.example.demo.dto.SubjectResponse
import com.example.demo.entities.Subject
import com.example.demo.exceptions.BlankNameException
import com.example.demo.exceptions.ProfessorNotFoundException
import com.example.demo.exceptions.SubjectNotFoundException
import com.example.demo.mappers.toResponse
import com.example.demo.repositories.ProfessorRepository
import com.example.demo.repositories.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class SubjectService(
    private val subjectRepository: SubjectRepository,
    private val professorRepository: ProfessorRepository
) {

    private val logger = LoggerFactory.getLogger(SubjectService::class.java)


    fun createSubject(request: SubjectRequest): SubjectResponse {

        if (request.name.isBlank()) {
            throw BlankNameException(
                "El nombre no puede estar vacío"
            )
        }


        val professor = professorRepository.findById(request.professorId)
            .orElseThrow {
                ProfessorNotFoundException(
                    "Profesor no encontrado con id: ${request.professorId}"
                )
            }


        logger.info("Creando materia: ${request.name}")


        val subject = subjectRepository.save(
            Subject(
                name = request.name,
                professor = professor
            )
        )

        return subject.toResponse()
    }


    fun getAllSubjects(): List<SubjectResponse> {

        logger.info("Obteniendo todas las materias")

        return subjectRepository.findAll().map {
            it.toResponse()
        }
    }


    fun getSubjectById(id: Long): SubjectResponse {

        val subject = subjectRepository.findById(id)
            .orElseThrow {
                SubjectNotFoundException(
                    "Materia no encontrada con id: $id"
                )
            }

        return subject.toResponse()
    }


    fun updateSubject(
        id: Long,
        request: SubjectRequest
    ): SubjectResponse {

        if (request.name.isBlank()) {
            throw BlankNameException(
                "El nombre no puede estar vacío"
            )
        }


        val subject = subjectRepository.findById(id)
            .orElseThrow {
                SubjectNotFoundException(
                    "Materia no encontrada con id: $id"
                )
            }


        val professor = professorRepository.findById(request.professorId)
            .orElseThrow {
                ProfessorNotFoundException(
                    "Profesor no encontrado con id: ${request.professorId}"
                )
            }


        val updatedSubject = Subject(
            id = subject.id,
            name = request.name,
            code = subject.code,
            professor = professor,
            enrollments = subject.enrollments
        )


        val savedSubject = subjectRepository.save(updatedSubject)

        return savedSubject.toResponse()
    }


    fun deleteSubject(id: Long) {

        val subject = subjectRepository.findById(id)
            .orElseThrow {
                SubjectNotFoundException(
                    "Materia no encontrada con id: $id"
                )
            }


        subjectRepository.delete(subject)
    }
}