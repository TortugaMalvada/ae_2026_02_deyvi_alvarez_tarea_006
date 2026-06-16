package com.example.demo.entities

import jakarta.persistence.*

@Entity
@Table(name = "enrollments")
open class Enrollment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    val student: Student,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    val subject: Subject,

    val status: String = "INSCRITO"
)