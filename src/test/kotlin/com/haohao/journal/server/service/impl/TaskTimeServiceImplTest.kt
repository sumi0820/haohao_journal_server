package com.haohao.journal.server.service.impl

import com.haohao.journal.server.model.Epic
import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.Task
import com.haohao.journal.server.model.TaskPriority
import com.haohao.journal.server.model.TaskStatus
import com.haohao.journal.server.repository.TaskRepository
import com.haohao.journal.server.service.SprintService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.Optional

class TaskTimeServiceImplTest {
    private val taskRepository: TaskRepository = mock()
    private val sprintService: SprintService = mock()
    private val service = TaskTimeServiceImpl(taskRepository, sprintService)

    private val dummySprint =
        Sprint(
            id = 1L,
            startDate = LocalDateTime.now(),
            endDate = LocalDateTime.now().plusDays(7),
        )

    private val dummyEpic =
        Epic(
            id = 1L,
            sprint = dummySprint,
            title = "Test Epic",
        )

    @Test
    fun `updateEstimatedHours should update task's estimated hours`() {
        // Given
        val taskId = 1L
        val hours = 5.0f
        val task =
            Task(
                id = taskId,
                sprint = dummySprint,
                epic = dummyEpic,
                title = "Test Task",
                plannedDate = LocalDateTime.now(),
                status = TaskStatus.TODO,
                priority = TaskPriority.MEDIUM,
            )

        whenever(taskRepository.findById(eq(taskId))).thenReturn(Optional.of(task))
        whenever(taskRepository.save(any<Task>())).thenReturn(task)

        // When
        val result = service.updateEstimatedHours(taskId, hours)

        // Then
        assertEquals(hours, result.estimatedHours)
        verify(taskRepository).findById(taskId)
        verify(taskRepository).save(any())
    }

    @Test
    fun `updateActualHours should update task's actual hours`() {
        // Given
        val taskId = 1L
        val hours = 3.0f
        val task =
            Task(
                id = taskId,
                sprint = dummySprint,
                epic = dummyEpic,
                title = "Test Task",
                plannedDate = LocalDateTime.now(),
                status = TaskStatus.TODO,
                priority = TaskPriority.MEDIUM,
            )

        whenever(taskRepository.findById(eq(taskId))).thenReturn(Optional.of(task))
        whenever(taskRepository.save(any<Task>())).thenReturn(task)

        // When
        val result = service.updateActualHours(taskId, hours)

        // Then
        assertEquals(hours, result.actualHours)
        verify(taskRepository).findById(taskId)
        verify(taskRepository).save(any())
    }

    @Test
    fun `sumEstimatedHoursBySprintAndStatus should return sum of estimated hours`() {
        // Given
        val sprintId = 1L
        val status = TaskStatus.DOING
        whenever(sprintService.findById(eq(sprintId))).thenReturn(dummySprint)
        whenever(taskRepository.sumEstimatedHoursBySprintAndStatus(eq(dummySprint), eq(status))).thenReturn(10.0f)

        // When
        val result = service.sumEstimatedHoursBySprintAndStatus(sprintId, status)

        // Then
        assertEquals(10.0f, result)
        verify(sprintService).findById(sprintId)
        verify(taskRepository).sumEstimatedHoursBySprintAndStatus(dummySprint, status)
    }

    @Test
    fun `sumActualHoursBySprintAndStatus should return sum of actual hours`() {
        // Given
        val sprintId = 1L
        val status = TaskStatus.DOING
        whenever(sprintService.findById(eq(sprintId))).thenReturn(dummySprint)
        whenever(taskRepository.sumActualHoursBySprintAndStatus(eq(dummySprint), eq(status))).thenReturn(6.0f)

        // When
        val result = service.sumActualHoursBySprintAndStatus(sprintId, status)

        // Then
        assertEquals(6.0f, result)
        verify(sprintService).findById(sprintId)
        verify(taskRepository).sumActualHoursBySprintAndStatus(dummySprint, status)
    }
}
