package com.example.demo
import com.example.demo.dto.ProfessorRequest
import com.example.demo.entities.Professor
import com.example.demo.exceptions.ProfessorNotFoundException
import com.example.demo.repositories.ProfessorRepository
import com.example.demo.services.ProfessorService
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
class ProfessorServiceTest {

	@Mock
	private lateinit var repository: ProfessorRepository

	@Test
	fun `createProfessor should return professor response when request is valid`() {

		// Arrange
		val request = ProfessorRequest(
			name = "Carlos",
			email = "carlos@puce.com"
		)

		val savedProfessor = Professor(
			id = 1L,
			name = "Carlos",
			email = "carlos@puce.com"
		)

		val service = ProfessorService(repository)

		`when`(repository.save(any(Professor::class.java)))
			.thenReturn(savedProfessor)

		// Act
		val response = service.createProfessor(request)

		// Assert
		assertEquals(1L, response.id)
		assertEquals("Carlos", response.name)
		assertEquals("carlos@puce.com", response.email)
	}

	@Test
	fun `getAllProfessors should return all professors`() {

		// Arrange
		val professors = listOf(
			Professor(
				id = 1L,
				name = "Carlos",
				email = "carlos@puce.com"
			),
			Professor(
				id = 2L,
				name = "Elena",
				email = "elena@puce.com"
			)
		)

		val service = ProfessorService(repository)

		`when`(repository.findAll())
			.thenReturn(professors)

		// Act
		val response = service.getAllProfessors()

		// Assert
		assertEquals(2, response.size)
		assertEquals("Carlos", response[0].name)
		assertEquals("Elena", response[1].name)
		assertEquals("elena@puce.com", response[1].email)
	}

	@Test
	fun `getProfessorById should return professor when id exists`() {

		// Arrange
		val professor = Professor(
			id = 1L,
			name = "Carlos",
			email = "carlos@puce.com"
		)

		val service = ProfessorService(repository)

		`when`(repository.findById(1L))
			.thenReturn(Optional.of(professor))

		// Act
		val response = service.getProfessorById(1L)

		// Assert
		assertEquals(1L, response.id)
		assertEquals("Carlos", response.name)
		assertEquals("carlos@puce.com", response.email)
	}

	@Test
	fun `getProfessorById should throw ProfessorNotFoundException when id does not exist`() {

		// Arrange
		val service = ProfessorService(repository)

		`when`(repository.findById(1L))
			.thenReturn(Optional.empty())

		// Act
		val exception = assertThrows(ProfessorNotFoundException::class.java) {
			service.getProfessorById(1L)
		}

		// Assert
		assertEquals(
			"Profesor no encontrado con id: 1",
			exception.message
		)
	}

	@Test
	fun `updateProfessor should return updated professor when id exists`() {

		// Arrange
		val existingProfessor = Professor(
			id = 1L,
			name = "Carlos",
			email = "carlos@puce.com"
		)

		val request = ProfessorRequest(
			name = "Carlos Updated",
			email = "carlos.updated@puce.com"
		)

		val updatedProfessor = Professor(
			id = 1L,
			name = "Carlos Updated",
			email = "carlos.updated@puce.com"
		)

		val service = ProfessorService(repository)

		`when`(repository.findById(1L))
			.thenReturn(Optional.of(existingProfessor))

		`when`(repository.save(any(Professor::class.java)))
			.thenReturn(updatedProfessor)

		// Act
		val response = service.updateProfessor(
			id = 1L,
			request = request
		)

		// Assert
		assertEquals(1L, response.id)
		assertEquals("Carlos Updated", response.name)
		assertEquals("carlos.updated@puce.com", response.email)
	}

	@Test
	fun `updateProfessor should throw ProfessorNotFoundException when id does not exist`() {

		// Arrange
		val request = ProfessorRequest(
			name = "Carlos Updated",
			email = "carlos.updated@puce.com"
		)

		val service = ProfessorService(repository)

		`when`(repository.findById(1L))
			.thenReturn(Optional.empty())

		// Act
		val exception = assertThrows(ProfessorNotFoundException::class.java) {
			service.updateProfessor(
				id = 1L,
				request = request
			)
		}

		// Assert
		assertEquals(
			"Profesor no encontrado con id: 1",
			exception.message
		)
	}

	@Test
	fun `deleteProfessor should delete professor when id exists`() {

		// Arrange
		val professor = Professor(
			id = 1L,
			name = "Carlos",
			email = "carlos@puce.com"
		)

		val service = ProfessorService(repository)

		`when`(repository.findById(1L))
			.thenReturn(Optional.of(professor))

		// Act
		service.deleteProfessor(1L)

		// Assert
		verify(repository).delete(professor)
	}

	@Test
	fun `deleteProfessor should throw ProfessorNotFoundException when id does not exist`() {

		// Arrange
		val service = ProfessorService(repository)

		`when`(repository.findById(1L))
			.thenReturn(Optional.empty())

		// Act
		val exception = assertThrows(ProfessorNotFoundException::class.java) {
			service.deleteProfessor(1L)
		}

		// Assert
		assertEquals(
			"Profesor no encontrado con id: 1",
			exception.message
		)
	}
}