package com.example.demo.entities

import jakarta.persistence.*

@Entity
@Table(name = "professors")
open class Professor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val name: String = "",
    val email: String = "",

    // uno a muchos
    @OneToMany(mappedBy = "professor", cascade = [CascadeType.ALL], orphanRemoval = true)
    val subjects: MutableList<Subject> = mutableListOf()
)