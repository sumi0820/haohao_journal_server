package com.haohao.journal.server.repository

import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.Task
import com.haohao.journal.server.model.TaskStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    fun findBySprint(sprint: Sprint): List<Task>

    fun findBySprintAndPlannedDateBetween(
        sprint: Sprint,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<Task>

    fun findBySprintAndStatus(
        sprint: Sprint,
        status: TaskStatus,
    ): List<Task>

    fun findBySprintAndPlannedDateBeforeAndStatusNot(
        sprint: Sprint,
        date: LocalDateTime,
        status: TaskStatus,
    ): List<Task>
}
