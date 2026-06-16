package com.example.demo.repositories

import com.example.demo.entities.Professor
import org.springframework.data.jpa.repository.JpaRepository

interface ProfessorRepository : JpaRepository<Professor, Long>