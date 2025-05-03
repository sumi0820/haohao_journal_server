package com.haohao.journal.server.service

import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.SprintStatus
import com.haohao.journal.server.repository.SprintRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.argThat
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class SprintServiceTest {
    @Mock
    private lateinit var sprintRepository: SprintRepository

    @InjectMocks
    private lateinit var sprintService: SprintService

    private val now = LocalDateTime.now()
    private val sprint =
        Sprint(
            id = 1L,
            startDate = now,
            endDate = now.plusWeeks(2),
        )

    @Test
    fun `findAll should return all sprints`() {
        val sprints = listOf(sprint)
        `when`(sprintRepository.findAll()).thenReturn(sprints)

        val result = sprintService.findAll()

        assertEquals(sprints, result)
    }

    @Test
    fun `findById should return sprint when exists`() {
        `when`(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint))

        val result = sprintService.findById(1L)

        assertEquals(sprint, result)
    }

    @Test
    fun `findById should return null when not exists`() {
        `when`(sprintRepository.findById(1L)).thenReturn(Optional.empty())

        val result = sprintService.findById(1L)

        assertNull(result)
    }

    @Test
    fun `create should return created sprint`() {
        `when`(
            sprintRepository.save(
                argThat { sprint ->
                    sprint.startDate == now &&
                        sprint.endDate == now.plusWeeks(2)
                },
            ),
        ).thenReturn(sprint)

        val result = sprintService.create(now, now.plusWeeks(2))

        assertEquals(sprint, result)
    }

    @Test
    fun `update should return updated sprint when exists`() {
        val updatedStartDate = now.plusDays(1)
        val updatedEndDate = now.plusWeeks(3)
        val updatedSprint = sprint.copy(startDate = updatedStartDate, endDate = updatedEndDate)

        `when`(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint))
        `when`(
            sprintRepository.save(
                argThat { sprint ->
                    sprint.startDate == updatedStartDate &&
                        sprint.endDate == updatedEndDate
                },
            ),
        ).thenReturn(updatedSprint)

        val result = sprintService.update(1L, updatedStartDate, updatedEndDate)

        assertEquals(updatedSprint, result)
    }

    @Test
    fun `update should throw exception when not exists`() {
        `when`(sprintRepository.findById(1L)).thenReturn(Optional.empty())

        try {
            sprintService.update(1L, now, now.plusWeeks(2))
        } catch (e: IllegalArgumentException) {
            assertEquals("Sprint not found with id: 1", e.message)
        }
    }

    @Test
    fun `updateStatus should return updated sprint when exists`() {
        val updatedSprint = sprint.copy(status = SprintStatus.IN_PROGRESS)

        `when`(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint))
        `when`(sprintRepository.save(any())).thenReturn(updatedSprint)

        val result = sprintService.updateStatus(1L, SprintStatus.IN_PROGRESS)

        assertEquals(updatedSprint, result)
    }

    @Test
    fun `updateStatus should throw exception when not exists`() {
        `when`(sprintRepository.findById(1L)).thenReturn(Optional.empty())

        try {
            sprintService.updateStatus(1L, SprintStatus.IN_PROGRESS)
        } catch (e: IllegalArgumentException) {
            assertEquals("Sprint not found with id: 1", e.message)
        }
    }

    @Test
    fun `delete should delete sprint when exists`() {
        `when`(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint))

        sprintService.delete(1L)

        verify(sprintRepository).delete(sprint)
    }

    @Test
    fun `delete should throw exception when not exists`() {
        `when`(sprintRepository.findById(1L)).thenReturn(Optional.empty())

        try {
            sprintService.delete(1L)
        } catch (e: IllegalArgumentException) {
            assertEquals("Sprint not found with id: 1", e.message)
        }
    }
}
