package com.haohao.journal.server.controller

import com.haohao.journal.server.model.Epic
import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.Task
import com.haohao.journal.server.model.TaskStatus
import com.haohao.journal.server.service.TaskTimeService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class TaskTimeControllerTest {
    private val taskTimeService: TaskTimeService = mock()
    private val controller = TaskTimeController(taskTimeService)

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
    fun `updateTaskEstimatedHours should update and return task`() {
        // Given
        val taskId = 1L
        val hours = 5.0f
        val expectedTask =
            Task(
                id = taskId,
                sprint = dummySprint,
                epic = dummyEpic,
                title = "Test Task",
                plannedDate = LocalDateTime.now(),
                estimatedHours = hours,
            )
        whenever(taskTimeService.updateEstimatedHours(eq(taskId), eq(hours))).thenReturn(expectedTask)

        // When
        val response = controller.updateTaskEstimatedHours(taskId, hours)

        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(expectedTask, response.body)
        verify(taskTimeService).updateEstimatedHours(taskId, hours)
    }

    @Test
    fun `updateTaskActualHours should update and return task`() {
        // Given
        val taskId = 1L
        val hours = 3.0f
        val expectedTask =
            Task(
                id = taskId,
                sprint = dummySprint,
                epic = dummyEpic,
                title = "Test Task",
                plannedDate = LocalDateTime.now(),
                actualHours = hours,
            )
        whenever(taskTimeService.updateActualHours(eq(taskId), eq(hours))).thenReturn(expectedTask)

        // When
        val response = controller.updateTaskActualHours(taskId, hours)

        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(expectedTask, response.body)
        verify(taskTimeService).updateActualHours(taskId, hours)
    }

    @Test
    fun `getEstimatedHoursBySprintAndStatus should return sum of estimated hours`() {
        // Given
        val sprintId = 1L
        val status = TaskStatus.DOING
        val expectedHours = 10.0f
        whenever(taskTimeService.sumEstimatedHoursBySprintAndStatus(eq(sprintId), eq(status))).thenReturn(expectedHours)

        // When
        val response = controller.getEstimatedHoursBySprintAndStatus(sprintId, status)

        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(expectedHours, response.body)
        verify(taskTimeService).sumEstimatedHoursBySprintAndStatus(sprintId, status)
    }

    @Test
    fun `getActualHoursBySprintAndStatus should return sum of actual hours`() {
        // Given
        val sprintId = 1L
        val status = TaskStatus.DOING
        val expectedHours = 8.0f
        whenever(taskTimeService.sumActualHoursBySprintAndStatus(eq(sprintId), eq(status))).thenReturn(expectedHours)

        // When
        val response = controller.getActualHoursBySprintAndStatus(sprintId, status)

        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(expectedHours, response.body)
        verify(taskTimeService).sumActualHoursBySprintAndStatus(sprintId, status)
    }
}
