package com.example.demo.entities

import jakarta.persistence.*

@Entity
@Table(name = "students")
open class Student(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val name: String = "",
    val email: String = "" ,

    @OneToMany(mappedBy = "student", cascade = [CascadeType.ALL], orphanRemoval = true)
    val enrollments: MutableList<Enrollment> = mutableListOf()
)