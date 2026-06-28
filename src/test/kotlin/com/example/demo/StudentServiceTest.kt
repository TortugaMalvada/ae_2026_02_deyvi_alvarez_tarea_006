package com.example.demo

import com.example.demo.dto.StudentRequest
import com.example.demo.entities.Student
import com.example.demo.exceptions.EmailAlreadyExistsException
import com.example.demo.exceptions.StudentNotFoundException
import com.example.demo.repositories.StudentRepository
import com.example.demo.services.StudentService
import kotlin.test.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class StudentServiceTest {

	@Mock
	private lateinit var repository: StudentRepository

	@Test
	fun `createStudent should return student response when email does not exist`() {
		// Arrange
		val request = StudentRequest(
			name = "Jeremy",
			email = "jeremy@puce.com"
		)

		val savedStudent = Student(
			id = 1L,
			name = "Jeremy",
			email = "jeremy@puce.com"
		)

		val service = StudentService(repository)

		`when`(repository.existsByEmail(request.email))
			.thenReturn(false)

		`when`(repository.save(any(Student::class.java)))
			.thenReturn(savedStudent)

		// Act
		val response = service.createStudent(request)

		// Assert
		assertEquals(1L, response.id)
		assertEquals("Jeremy", response.name)
		assertEquals("jeremy@puce.com", response.email)
	}

	@Test
	fun `createStudent should throw EmailAlreadyExistsException when email already exists`() {

		// Arrange
		val request = StudentRequest(
			name = "Jeremy",
			email = "jeremy@puce.com"
		)

		val service = StudentService(repository)

		`when`(repository.existsByEmail(request.email))
			.thenReturn(true)

		// Act
		val exception = assertThrows(EmailAlreadyExistsException::class.java) {
			service.createStudent(request)
		}

		// Assert
		assertEquals(
			"El email ya existe",
			exception.message
		)
	}

	@Test
	fun `getAllStudents should return all students`() {

		// Arrange
		val students = listOf(
			Student(
				id = 1L,
				name = "Jeremy",
				email = "jeremy@puce.com"
			),
			Student(
				id = 2L,
				name = "Ana",
				email = "ana@puce.com"
			)
		)

		val service = StudentService(repository)

		`when`(repository.findAll())
			.thenReturn(students)

		// Act
		val response = service.getAllStudents()

		// Assert
		assertEquals(2, response.size)
		assertEquals("Jeremy", response[0].name)
		assertEquals("Ana", response[1].name)
		assertEquals("ana@puce.com", response[1].email)
	}

	@Test
	fun `getStudentById should return student when id exists`() {

		// Arrange
		val student = Student(
			id = 1L,
			name = "Jeremy",
			email = "jeremy@puce.com"
		)

		val service = StudentService(repository)

		`when`(repository.findById(1L))
			.thenReturn(Optional.of(student))

		// Act
		val response = service.getStudentById(1L)

		// Assert
		assertEquals(1L, response.id)
		assertEquals("Jeremy", response.name)
		assertEquals("jeremy@puce.com", response.email)
	}

	@Test
	fun `getStudentById should throw StudentNotFoundException when id does not exist`() {

		// Arrange
		val service = StudentService(repository)

		`when`(repository.findById(1L))
			.thenReturn(Optional.empty())

		// Act
		val exception = assertThrows(StudentNotFoundException::class.java) {
			service.getStudentById(1L)
		}

		// Assert
		assertEquals(
			"Estudiante no encontrado con id: 1",
			exception.message
		)
	}

	@Test
	fun `updateStudent should return updated student when id exists`() {

		// Arrange
		val existingStudent = Student(
			id = 1L,
			name = "Jeremy",
			email = "jeremy@puce.com"
		)

		val request = StudentRequest(
			name = "Jeremy Updated",
			email = "jeremy.updated@puce.com"
		)

		val updatedStudent = Student(
			id = 1L,
			name = "Jeremy Updated",
			email = "jeremy.updated@puce.com"
		)

		val service = StudentService(repository)

		`when`(repository.findById(1L))
			.thenReturn(Optional.of(existingStudent))

		`when`(repository.save(any(Student::class.java)))
			.thenReturn(updatedStudent)

		// Act
		val response = service.updateStudent(
			id = 1L,
			request = request
		)

		// Assert
		assertEquals(1L, response.id)
		assertEquals("Jeremy Updated", response.name)
		assertEquals("jeremy.updated@puce.com", response.email)
	}

	@Test
	fun `updateStudent should throw StudentNotFoundException when id does not exist`() {

		// Arrange
		val request = StudentRequest(
			name = "Jeremy Updated",
			email = "jeremy.updated@puce.com"
		)

		val service = StudentService(repository)

		`when`(repository.findById(1L))
			.thenReturn(Optional.empty())

		// Act
		val exception = assertThrows(StudentNotFoundException::class.java) {
			service.updateStudent(
				id = 1L,
				request = request
			)
		}

		// Assert
		assertEquals(
			"Estudiante no encontrado con id: 1",
			exception.message
		)
	}

	@Test
	fun `deleteStudent should delete student when id exists`() {

		// Arrange
		val student = Student(
			id = 1L,
			name = "Jeremy",
			email = "jeremy@puce.com"
		)

		val service = StudentService(repository)

		`when`(repository.findById(1L))
			.thenReturn(Optional.of(student))

		// Act
		service.deleteStudent(1L)

		// Assert
		verify(repository).delete(student)
	}

	@Test
	fun `deleteStudent should throw StudentNotFoundException when id does not exist`() {

		// Arrange
		val service = StudentService(repository)

		`when`(repository.findById(1L))
			.thenReturn(Optional.empty())

		// Act
		val exception = assertThrows(StudentNotFoundException::class.java) {
			service.deleteStudent(1L)
		}

		// Assert
		assertEquals(
			"Estudiante no encontrado con id: 1",
			exception.message
		)
	}
}