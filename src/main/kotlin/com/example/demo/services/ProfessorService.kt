package com.example.demo.services

import com.example.demo.dto.ProfessorRequest
import com.example.demo.dto.ProfessorResponse
import com.example.demo.entities.Professor
import com.example.demo.exceptions.ProfessorNotFoundException
import com.example.demo.mappers.toEntity
import com.example.demo.mappers.toResponse
import com.example.demo.repositories.ProfessorRepository
import org.springframework.stereotype.Service


@Service
class ProfessorService(
    private val repository: ProfessorRepository
) {


    fun createProfessor(request: ProfessorRequest): ProfessorResponse {

        val professorToSave = request.toEntity()

        val savedProfessor = repository.save(professorToSave)

        return savedProfessor.toResponse()
    }


    fun getAllProfessors(): List<ProfessorResponse> {

        return repository.findAll().map {
            it.toResponse()
        }
    }


    fun getProfessorById(id: Long): ProfessorResponse {

        val professor = repository.findById(id)
            .orElseThrow {
                ProfessorNotFoundException(
                    "Profesor no encontrado con id: $id"
                )
            }

        return professor.toResponse()
    }


    fun updateProfessor(
        id: Long,
        request: ProfessorRequest
    ): ProfessorResponse {

        val professor = repository.findById(id)
            .orElseThrow {
                ProfessorNotFoundException(
                    "Profesor no encontrado con id: $id"
                )
            }


        val updatedProfessor = Professor(
            id = professor.id,
            name = request.name,
            email = request.email,
            subjects = professor.subjects
        )


        val savedProfessor = repository.save(updatedProfessor)

        return savedProfessor.toResponse()
    }


    fun deleteProfessor(id: Long) {

        val professor = repository.findById(id)
            .orElseThrow {
                ProfessorNotFoundException(
                    "Profesor no encontrado con id: $id"
                )
            }

        repository.delete(professor)
    }
}