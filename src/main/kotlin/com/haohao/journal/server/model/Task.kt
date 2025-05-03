package com.haohao.journal.server.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

enum class TaskStatus {
    TODO,
    DOING,
    DONE,
}

@Entity
@Table(name = "tasks")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne
    @JoinColumn(name = "sprint_id", nullable = false)
    val sprint: Sprint,
    @ManyToOne
    @JoinColumn(name = "epic_id", nullable = false)
    var epic: Epic,
    @Column(nullable = false)
    var title: String,
    @Column
    var memo: String? = null,
    @Column(nullable = false)
    var plannedDate: LocalDateTime,
    @Column
    var completedDate: LocalDateTime? = null,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: TaskStatus = TaskStatus.TODO,
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
