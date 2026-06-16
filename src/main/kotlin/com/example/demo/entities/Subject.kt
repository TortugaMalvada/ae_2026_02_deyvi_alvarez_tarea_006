package com.example.demo.entities

import jakarta.persistence.*

@Entity
@Table(name = "subjects")
open class Subject(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val name: String = "",
    val code: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    val professor: Professor,

    @OneToMany(mappedBy = "subject", cascade = [CascadeType.ALL], orphanRemoval = true)
    val enrollments: MutableList<Enrollment> = mutableListOf()
)