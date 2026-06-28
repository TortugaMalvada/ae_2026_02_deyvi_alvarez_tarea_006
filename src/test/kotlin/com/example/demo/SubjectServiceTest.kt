package com.example.demo

import com.example.demo.dto.SubjectRequest
import com.example.demo.entities.Professor
import com.example.demo.entities.Subject
import com.example.demo.exceptions.BlankNameException
import com.example.demo.exceptions.ProfessorNotFoundException
import com.example.demo.exceptions.SubjectNotFoundException
import com.example.demo.repositories.ProfessorRepository
import com.example.demo.repositories.SubjectRepository
import com.example.demo.services.SubjectService
import kotlin.test.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional
import org.mockito.Mockito.verify

@ExtendWith(MockitoExtension::class)
class SubjectServiceTest {

	@Mock
	private lateinit var subjectRepository: SubjectRepository

	@Mock
	private lateinit var professorRepository: ProfessorRepository

	@Test
	fun `createSubject should create subject successfully`() {

		// Arrange
		val professor = Professor(
			id = 1L,
			name = "Carlos",
			email = "carlos@puce.com"
		)

		val request = SubjectRequest(
			name = "Arquitectura",
			professorId = 1L
		)

		val savedSubject = Subject(
			id = 1L,
			name = "Arquitectura",
			code = "",
			professor = professor
		)

		val service = SubjectService(
			subjectRepository,
			professorRepository
		)

		`when`(professorRepository.findById(1L))
			.thenReturn(Optional.of(professor))

		`when`(subjectRepository.save(any(Subject::class.java)))
			.thenReturn(savedSubject)

		// Act
		val response = service.createSubject(request)

		// Assert
		assertEquals(1L, response.id)
		assertEquals("Arquitectura", response.name)
		assertEquals("Carlos", response.professorName)
	}

	@Test
	fun `createSubject should throw BlankNameException when name is blank`() {

		val service = SubjectService(
			subjectRepository,
			professorRepository
		)

		val request = SubjectRequest(
			name = "",
			professorId = 1L
		)

		val exception = assertThrows(BlankNameException::class.java) {
			service.createSubject(request)
		}

		assertEquals(
			"El nombre no puede estar vacío",
			exception.message
		)
	}

	@Test
	fun `createSubject should throw ProfessorNotFoundException when professor does not exist`() {

		val service = SubjectService(
			subjectRepository,
			professorRepository
		)

		val request = SubjectRequest(
			name = "Arquitectura",
			professorId = 1L
		)

		`when`(professorRepository.findById(1L))
			.thenReturn(Optional.empty())

		val exception = assertThrows(ProfessorNotFoundException::class.java) {
			service.createSubject(request)
		}

		assertEquals(
			"Profesor no encontrado con id: 1",
			exception.message
		)
	}

	@Test
	fun `getAllSubjects should return all subjects`() {

		val professor = Professor(
			id = 1L,
			name = "Carlos",
			email = "carlos@puce.com"
		)

		val subjects = listOf(

			Subject(
				id = 1L,
				name = "Arquitectura",
				code = "",
				professor = professor
			),

			Subject(
				id = 2L,
				name = "Programacion",
				code = "",
				professor = professor
			)
		)

		val service = SubjectService(
			subjectRepository,
			professorRepository
		)

		`when`(subjectRepository.findAll())
			.thenReturn(subjects)

		val response = service.getAllSubjects()

		assertEquals(2, response.size)
		assertEquals("Arquitectura", response[0].name)
		assertEquals("Programacion", response[1].name)
	}

	@Test
	fun `getSubjectById should return subject when id exists`() {

		val professor = Professor(
			id = 1L,
			name = "Carlos",
			email = "carlos@puce.com"
		)

		val subject = Subject(
			id = 1L,
			name = "Arquitectura",
			code = "",
			professor = professor
		)

		val service = SubjectService(
			subjectRepository,
			professorRepository
		)

		`when`(subjectRepository.findById(1L))
			.thenReturn(Optional.of(subject))

		val response = service.getSubjectById(1L)

		assertEquals(1L, response.id)
		assertEquals("Arquitectura", response.name)
		assertEquals("Carlos", response.professorName)
	}

	@Test
	fun `getSubjectById should throw SubjectNotFoundException when id does not exist`() {

		val service = SubjectService(
			subjectRepository,
			professorRepository
		)

		`when`(subjectRepository.findById(1L))
			.thenReturn(Optional.empty())

		val exception = assertThrows(SubjectNotFoundException::class.java) {
			service.getSubjectById(1L)
		}

		assertEquals(
			"Materia no encontrada con id: 1",
			exception.message
		)
	}
	@Test
	fun `updateSubject should update subject successfully`() {

		val oldProfessor = Professor(
			id = 1L,
			name = "Carlos",
			email = "carlos@puce.com"
		)

		val newProfessor = Professor(
			id = 2L,
			name = "Elena",
			email = "elena@puce.com"
		)

		val existingSubject = Subject(
			id = 1L,
			name = "Arquitectura",
			code = "",
			professor = oldProfessor
		)

		val request = SubjectRequest(
			name = "Arquitectura Avanzada",
			professorId = 2L
		)

		val updatedSubject = Subject(
			id = 1L,
			name = "Arquitectura Avanzada",
			code = "",
			professor = newProfessor
		)

		val service = SubjectService(
			subjectRepository,
			professorRepository
		)

		`when`(subjectRepository.findById(1L))
			.thenReturn(Optional.of(existingSubject))

		`when`(professorRepository.findById(2L))
			.thenReturn(Optional.of(newProfessor))

		`when`(subjectRepository.save(any(Subject::class.java)))
			.thenReturn(updatedSubject)

		val response = service.updateSubject(1L, request)

		assertEquals(1L, response.id)
		assertEquals("Arquitectura Avanzada", response.name)
		assertEquals("Elena", response.professorName)
	}

	@Test
	fun `updateSubject should throw BlankNameException when name is blank`() {

		val service = SubjectService(
			subjectRepository,
			professorRepository
		)

		val request = SubjectRequest(
			name = "",
			professorId = 1L
		)

		val exception = assertThrows(BlankNameException::class.java) {
			service.updateSubject(1L, request)
		}

		assertEquals(
			"El nombre no puede estar vacío",
			exception.message
		)
	}

	@Test
	fun `updateSubject should throw SubjectNotFoundException when subject does not exist`() {

		val service = SubjectService(
			subjectRepository,
			professorRepository
		)

		val request = SubjectRequest(
			name = "Arquitectura",
			professorId = 1L
		)

		`when`(subjectRepository.findById(1L))
			.thenReturn(Optional.empty())

		val exception = assertThrows(SubjectNotFoundException::class.java) {
			service.updateSubject(1L, request)
		}

		assertEquals(
			"Materia no encontrada con id: 1",
			exception.message
		)
	}

	@Test
	fun `updateSubject should throw ProfessorNotFoundException when professor does not exist`() {

		val professor = Professor(
			id = 1L,
			name = "Carlos",
			email = "carlos@puce.com"
		)

		val subject = Subject(
			id = 1L,
			name = "Arquitectura",
			code = "",
			professor = professor
		)

		val request = SubjectRequest(
			name = "Arquitectura",
			professorId = 2L
		)

		val service = SubjectService(
			subjectRepository,
			professorRepository
		)

		`when`(subjectRepository.findById(1L))
			.thenReturn(Optional.of(subject))

		`when`(professorRepository.findById(2L))
			.thenReturn(Optional.empty())

		val exception = assertThrows(ProfessorNotFoundException::class.java) {
			service.updateSubject(1L, request)
		}

		assertEquals(
			"Profesor no encontrado con id: 2",
			exception.message
		)
	}

	@Test
	fun `deleteSubject should delete subject successfully`() {

		val professor = Professor(
			id = 1L,
			name = "Carlos",
			email = "carlos@puce.com"
		)

		val subject = Subject(
			id = 1L,
			name = "Arquitectura",
			code = "",
			professor = professor
		)

		val service = SubjectService(
			subjectRepository,
			professorRepository
		)

		`when`(subjectRepository.findById(1L))
			.thenReturn(Optional.of(subject))

		service.deleteSubject(1L)

		verify(subjectRepository).delete(subject)
	}

	@Test
	fun `deleteSubject should throw SubjectNotFoundException when subject does not exist`() {

		val service = SubjectService(
			subjectRepository,
			professorRepository
		)

		`when`(subjectRepository.findById(1L))
			.thenReturn(Optional.empty())

		val exception = assertThrows(SubjectNotFoundException::class.java) {
			service.deleteSubject(1L)
		}

		assertEquals(
			"Materia no encontrada con id: 1",
			exception.message
		)
	}

}