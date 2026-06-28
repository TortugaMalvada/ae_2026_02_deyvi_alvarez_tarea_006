package com.example.demo

import com.example.demo.dto.EnrollmentRequest
import com.example.demo.entities.Enrollment
import com.example.demo.entities.Professor
import com.example.demo.entities.Student
import com.example.demo.entities.Subject
import com.example.demo.exceptions.EnrollmentNotFoundException
import com.example.demo.exceptions.StudentNotFoundException
import com.example.demo.exceptions.SubjectNotFoundException
import com.example.demo.repositories.EnrollmentRepository
import com.example.demo.repositories.StudentRepository
import com.example.demo.repositories.SubjectRepository
import com.example.demo.services.EnrollmentService
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
class EnrollmentServiceTest {

    @Mock
    lateinit var enrollmentRepository: EnrollmentRepository

    @Mock
    lateinit var studentRepository: StudentRepository

    @Mock
    lateinit var subjectRepository: SubjectRepository

    @Test
    fun `createEnrollment should create enrollment successfully`() {
        val student = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val subject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val enrollment = Enrollment(id = 10L, student = student, subject = subject, status = "INSCRITO")
        val request = EnrollmentRequest(studentId = 5L, subjectId = 8L)

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)

        `when`(studentRepository.findById(5L)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(8L)).thenReturn(Optional.of(subject))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(enrollment)

        val response = service.createEnrollment(request)

        assertEquals(10L, response.id)
        assertEquals("Mateo", response.student.name)
        assertEquals("Estructuras", response.subject.name)
        assertEquals("INSCRITO", response.status)
    }

    @Test
    fun `createEnrollment should throw exception when student id is null`() {
        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)
        val request = EnrollmentRequest(studentId = null, subjectId = 8L)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            service.createEnrollment(request)
        }

        assertEquals("El id del estudiante es obligatorio", exception.message)
    }

    @Test
    fun `createEnrollment should throw exception when subject id is null`() {
        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)
        val request = EnrollmentRequest(studentId = 5L, subjectId = null)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            service.createEnrollment(request)
        }

        assertEquals("El id de la materia es obligatorio", exception.message)
    }

    @Test
    fun `createEnrollment should throw StudentNotFoundException`() {
        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)
        val request = EnrollmentRequest(studentId = 5L, subjectId = 8L)

        `when`(studentRepository.findById(5L)).thenReturn(Optional.empty())

        val exception = assertThrows(StudentNotFoundException::class.java) {
            service.createEnrollment(request)
        }

        assertEquals("Estudiante no encontrado con id: 5", exception.message)
    }

    @Test
    fun `createEnrollment should throw SubjectNotFoundException`() {
        val student = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)
        val request = EnrollmentRequest(studentId = 5L, subjectId = 8L)

        `when`(studentRepository.findById(5L)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(8L)).thenReturn(Optional.empty())

        val exception = assertThrows(SubjectNotFoundException::class.java) {
            service.createEnrollment(request)
        }

        assertEquals("Materia no encontrada con id: 8", exception.message)
    }

    @Test
    fun `updateEnrollment should change subject`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val student = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val oldSubject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val newSubject = Subject(id = 9L, name = "Redes", code = "RED-12", professor = professor)
        val enrollment = Enrollment(id = 10L, student = student, subject = oldSubject, status = "INSCRITO")
        val request = EnrollmentRequest(studentId = null, subjectId = 9L, status = null)
        val updated = Enrollment(id = 10L, student = student, subject = newSubject, status = "INSCRITO")

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))
        `when`(subjectRepository.findById(9L)).thenReturn(Optional.of(newSubject))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(updated)

        val response = service.updateEnrollment(10L, request)

        assertEquals("Redes", response.subject.name)
        assertEquals("Mateo", response.student.name)
    }

    @Test
    fun `updateEnrollment should throw SubjectNotFoundException when subject does not exist`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val student = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val oldSubject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val enrollment = Enrollment(id = 10L, student = student, subject = oldSubject, status = "INSCRITO")
        val request = EnrollmentRequest(studentId = null, subjectId = 9L, status = null)

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))
        `when`(subjectRepository.findById(9L)).thenReturn(Optional.empty())

        val exception = assertThrows(SubjectNotFoundException::class.java) {
            service.updateEnrollment(10L, request)
        }

        assertEquals("Materia no encontrada con id: 9", exception.message)
    }

    @Test
    fun `getAllEnrollments should return all enrollments`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val student = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val subject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val enrollments = listOf(Enrollment(id = 10L, student = student, subject = subject, status = "INSCRITO"))

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)
        `when`(enrollmentRepository.findAll()).thenReturn(enrollments)

        val response = service.getAllEnrollments()

        assertEquals(1, response.size)
        assertEquals("Mateo", response[0].student.name)
        assertEquals("Estructuras", response[0].subject.name)
    }

    @Test
    fun `getEnrollmentById should return enrollment`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val student = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val subject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val enrollment = Enrollment(id = 10L, student = student, subject = subject, status = "INSCRITO")

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)
        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))

        val response = service.getEnrollmentById(10L)

        assertEquals(10L, response.id)
        assertEquals("Mateo", response.student.name)
        assertEquals("Estructuras", response.subject.name)
    }

    @Test
    fun `getEnrollmentById should throw EnrollmentNotFoundException`() {
        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)
        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.empty())

        val exception = assertThrows(EnrollmentNotFoundException::class.java) {
            service.getEnrollmentById(10L)
        }

        assertEquals("Enrollment no encontrado con id: 10", exception.message)
    }

    @Test
    fun `updateEnrollment should update status only`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val student = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val subject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val enrollment = Enrollment(id = 10L, student = student, subject = subject, status = "INSCRITO")
        val updated = Enrollment(id = 10L, student = student, subject = subject, status = "APROBADO")
        val request = EnrollmentRequest(studentId = null, subjectId = null, status = "APROBADO")

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(updated)

        val response = service.updateEnrollment(10L, request)

        assertEquals("APROBADO", response.status)
        assertEquals("Mateo", response.student.name)
    }

    @Test
    fun `updateEnrollment should keep current status when status is null`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val student = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val subject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val enrollment = Enrollment(id = 10L, student = student, subject = subject, status = "INSCRITO")
        val request = EnrollmentRequest(studentId = null, subjectId = null, status = null)

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenAnswer { it.arguments[0] }

        val response = service.updateEnrollment(10L, request)

        assertEquals("INSCRITO", response.status)
    }

    @Test
    fun `updateEnrollment should throw EnrollmentNotFoundException`() {
        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)
        val request = EnrollmentRequest()

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.empty())

        val exception = assertThrows(EnrollmentNotFoundException::class.java) {
            service.updateEnrollment(10L, request)
        }

        assertEquals("Enrollment no encontrado con id: 10", exception.message)
    }

    @Test
    fun `updateEnrollment should change student`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val oldStudent = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val newStudent = Student(id = 6L, name = "Valeria", email = "valeria@puce.com")
        val subject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val enrollment = Enrollment(id = 10L, student = oldStudent, subject = subject, status = "INSCRITO")
        val request = EnrollmentRequest(studentId = 6L, subjectId = null, status = null)
        val updated = Enrollment(id = 10L, student = newStudent, subject = subject, status = "INSCRITO")

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))
        `when`(studentRepository.findById(6L)).thenReturn(Optional.of(newStudent))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(updated)

        val response = service.updateEnrollment(10L, request)

        assertEquals("Valeria", response.student.name)
    }

    @Test
    fun `updateEnrollment should throw StudentNotFoundException`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val student = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val subject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val enrollment = Enrollment(id = 10L, student = student, subject = subject, status = "INSCRITO")
        val request = EnrollmentRequest(studentId = 6L, subjectId = null, status = null)

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))
        `when`(studentRepository.findById(6L)).thenReturn(Optional.empty())

        val exception = assertThrows(StudentNotFoundException::class.java) {
            service.updateEnrollment(10L, request)
        }

        assertEquals("Estudiante no encontrado con id: 6", exception.message)
    }

    @Test
    fun `updateEnrollment should throw SubjectNotFoundException when updating to non-existing subject`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val student = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val oldSubject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val enrollment = Enrollment(id = 10L, student = student, subject = oldSubject, status = "INSCRITO")
        val request = EnrollmentRequest(studentId = null, subjectId = 999L, status = null)

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))
        `when`(subjectRepository.findById(999L)).thenReturn(Optional.empty())

        val exception = assertThrows(SubjectNotFoundException::class.java) {
            service.updateEnrollment(10L, request)
        }

        assertEquals("Materia no encontrada con id: 999", exception.message)
    }

    @Test
    fun `updateEnrollment should throw StudentNotFoundException when updating to non-existing student`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val student = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val subject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val enrollment = Enrollment(id = 10L, student = student, subject = subject, status = "INSCRITO")
        val request = EnrollmentRequest(studentId = 999L, subjectId = null, status = null)

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))
        `when`(studentRepository.findById(999L)).thenReturn(Optional.empty())

        val exception = assertThrows(StudentNotFoundException::class.java) {
            service.updateEnrollment(10L, request)
        }

        assertEquals("Estudiante no encontrado con id: 999", exception.message)
    }

    @Test
    fun `updateEnrollment should update both student and subject successfully`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val oldStudent = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val newStudent = Student(id = 6L, name = "Valeria", email = "valeria@puce.com")
        val oldSubject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val newSubject = Subject(id = 9L, name = "Redes", code = "RED-12", professor = professor)
        val enrollment = Enrollment(id = 10L, student = oldStudent, subject = oldSubject, status = "INSCRITO")
        val updated = Enrollment(id = 10L, student = newStudent, subject = newSubject, status = "INSCRITO")
        val request = EnrollmentRequest(studentId = 6L, subjectId = 9L, status = null)

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))
        `when`(studentRepository.findById(6L)).thenReturn(Optional.of(newStudent))
        `when`(subjectRepository.findById(9L)).thenReturn(Optional.of(newSubject))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(updated)

        val response = service.updateEnrollment(10L, request)

        assertEquals("Valeria", response.student.name)
        assertEquals("Redes", response.subject.name)
    }

    @Test
    fun `updateEnrollment should keep current student and subject when IDs are zero`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val student = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val subject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val enrollment = Enrollment(id = 10L, student = student, subject = subject, status = "INSCRITO")
        val request = EnrollmentRequest(studentId = 0L, subjectId = 0L, status = "APROBADO")

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenAnswer { it.arguments[0] }

        val response = service.updateEnrollment(10L, request)

        assertEquals("Mateo", response.student.name)
        assertEquals("Estructuras", response.subject.name)
        assertEquals("APROBADO", response.status)
    }

    @Test
    fun `deleteEnrollment should delete successfully`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val student = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val subject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val enrollment = Enrollment(id = 10L, student = student, subject = subject, status = "INSCRITO")

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)
        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))

        service.deleteEnrollment(10L)

        verify(enrollmentRepository).delete(enrollment)
    }

    @Test
    fun `deleteEnrollment should throw EnrollmentNotFoundException`() {
        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)
        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.empty())

        assertThrows(EnrollmentNotFoundException::class.java) {
            service.deleteEnrollment(10L)
        }
    }

    @Test
    fun `updateEnrollment should handle mixed zero and null ids - scenario student null subject zero`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val student = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val subject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val enrollment = Enrollment(id = 10L, student = student, subject = subject, status = "INSCRITO")
        val request = EnrollmentRequest(studentId = null, subjectId = 0L, status = null)

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenAnswer { it.arguments[0] }

        val response = service.updateEnrollment(10L, request)
        assertEquals("Mateo", response.student.name)
        assertEquals("Estructuras", response.subject.name)
    }

    @Test
    fun `updateEnrollment should handle mixed zero and null ids - scenario student zero subject null`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val student = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val subject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val enrollment = Enrollment(id = 10L, student = student, subject = subject, status = "INSCRITO")
        val request = EnrollmentRequest(studentId = 0L, subjectId = null, status = null)

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenAnswer { it.arguments[0] }

        val response = service.updateEnrollment(10L, request)
        assertEquals("Mateo", response.student.name)
        assertEquals("Estructuras", response.subject.name)
    }
    @Test
    fun `updateEnrollment should update subject but keep student when student ID is zero`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val student = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val oldSubject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val newSubject = Subject(id = 9L, name = "Redes", code = "RED-12", professor = professor)
        val enrollment = Enrollment(id = 10L, student = student, subject = oldSubject, status = "INSCRITO")
        val updated = Enrollment(id = 10L, student = student, subject = newSubject, status = "INSCRITO")

        // studentId es 0L (evalúa true && false), subjectId es válido (evalúa true && true)
        val request = EnrollmentRequest(studentId = 0L, subjectId = 9L, status = null)

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))
        `when`(subjectRepository.findById(9L)).thenReturn(Optional.of(newSubject))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(updated)

        val response = service.updateEnrollment(10L, request)

        assertEquals("Mateo", response.student.name)
        assertEquals("Redes", response.subject.name)
    }

    @Test
    fun `updateEnrollment should update student but keep subject when subject ID is zero`() {
        val professor = Professor(id = 3L, name = "Elena", email = "elena@puce.com")
        val oldStudent = Student(id = 5L, name = "Mateo", email = "mateo@puce.com")
        val newStudent = Student(id = 6L, name = "Valeria", email = "valeria@puce.com")
        val subject = Subject(id = 8L, name = "Estructuras", code = "EST-44", professor = professor)
        val enrollment = Enrollment(id = 10L, student = oldStudent, subject = subject, status = "INSCRITO")
        val updated = Enrollment(id = 10L, student = newStudent, subject = subject, status = "INSCRITO")

        // studentId es válido (true && true), subjectId es 0L (true && false)
        val request = EnrollmentRequest(studentId = 6L, subjectId = 0L, status = null)

        val service = EnrollmentService(enrollmentRepository, studentRepository, subjectRepository)

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))
        `when`(studentRepository.findById(6L)).thenReturn(Optional.of(newStudent))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(updated)

        val response = service.updateEnrollment(10L, request)

        assertEquals("Valeria", response.student.name)
        assertEquals("Estructuras", response.subject.name)
    }
}