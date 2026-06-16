package com.example.demo.repositories

import com.example.demo.entities.Subject
import org.springframework.data.jpa.repository.JpaRepository

interface SubjectRepository : JpaRepository<Subject, Long>