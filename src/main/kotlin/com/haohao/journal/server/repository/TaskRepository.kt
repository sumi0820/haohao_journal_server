package com.haohao.journal.server.repository

import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.Task
import com.haohao.journal.server.model.TaskPriority
import com.haohao.journal.server.model.TaskStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
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

    fun findBySprintAndPriority(
        sprint: Sprint,
        priority: TaskPriority,
    ): List<Task>

    fun findBySprintAndEstimatedHoursGreaterThan(
        sprint: Sprint,
        hours: Float,
    ): List<Task>

    fun findBySprintAndActualHoursGreaterThan(
        sprint: Sprint,
        hours: Float,
    ): List<Task>

    @Query(
        """
        SELECT t FROM Task t
        WHERE t.sprint = :sprint
        AND t.status = :status
        AND t.priority = :priority
        ORDER BY t.plannedDate ASC
        """,
    )
    fun findBySprintAndStatusAndPriorityOrderByPlannedDateAsc(
        @Param("sprint") sprint: Sprint,
        @Param("status") status: TaskStatus,
        @Param("priority") priority: TaskPriority,
    ): List<Task>

    @Query(
        """
        SELECT SUM(t.estimatedHours) FROM Task t
        WHERE t.sprint = :sprint
        AND t.status = :status
        """,
    )
    fun sumEstimatedHoursBySprintAndStatus(
        @Param("sprint") sprint: Sprint,
        @Param("status") status: TaskStatus,
    ): Float

    @Query(
        """
        SELECT SUM(t.actualHours) FROM Task t
        WHERE t.sprint = :sprint
        AND t.status = :status
        """,
    )
    fun sumActualHoursBySprintAndStatus(
        @Param("sprint") sprint: Sprint,
        @Param("status") status: TaskStatus,
    ): Float
}
