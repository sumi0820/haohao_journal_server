package com.haohao.journal.server.service

import com.haohao.journal.server.dto.TaskCreateRequest
import com.haohao.journal.server.dto.TaskUpdateRequest
import com.haohao.journal.server.model.Task
import com.haohao.journal.server.model.TaskPriority
import com.haohao.journal.server.model.TaskStatus
import java.time.LocalDateTime

interface TaskService {
    fun create(request: TaskCreateRequest): Task

    fun update(
        id: Long,
        request: TaskUpdateRequest,
    ): Task

    fun delete(id: Long)

    fun findById(id: Long): Task?

    fun findAllBySprint(sprintId: Long): List<Task>

    fun findBySprintAndDateBetween(
        sprintId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<Task>

    fun findBySprintAndStatus(
        sprintId: Long,
        status: TaskStatus,
    ): List<Task>

    fun findBySprintAndPriority(
        sprintId: Long,
        priority: TaskPriority,
    ): List<Task>

    fun findBySprintAndStatusAndPriorityOrderByPlannedDateAsc(
        sprintId: Long,
        status: TaskStatus,
        priority: TaskPriority,
    ): List<Task>

    fun updatePriority(
        id: Long,
        priority: TaskPriority,
    ): Task
}
