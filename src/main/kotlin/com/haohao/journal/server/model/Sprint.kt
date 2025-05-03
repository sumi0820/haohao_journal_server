package com.haohao.journal.server.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

enum class SprintStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
}

@Entity
@Table(name = "sprints")
data class Sprint(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false)
    val startDate: LocalDateTime,
    @Column(nullable = false)
    val endDate: LocalDateTime,
    @Column(nullable = false)
    val status: SprintStatus = SprintStatus.NOT_STARTED,
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun overlaps(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): Boolean {
        return !(this.endDate.isBefore(startDate) || this.startDate.isAfter(endDate))
    }
}
