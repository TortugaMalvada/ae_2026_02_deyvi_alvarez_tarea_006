package com.example.demo.repositories

import com.example.demo.entities.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository : JpaRepository<Student, Long> {
    fun existsByEmail(email: String): Boolean
}