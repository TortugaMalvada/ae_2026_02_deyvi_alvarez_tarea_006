package com.example.demo.repositories

import com.example.demo.entities.Enrollment
import org.springframework.data.jpa.repository.JpaRepository

interface EnrollmentRepository : JpaRepository<Enrollment, Long>