package com.haohao.journal.server.validation

import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.SprintStatus
import com.haohao.journal.server.service.SprintService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class SprintDateValidatorTest {
    @Mock
    private lateinit var sprintService: SprintService

    @InjectMocks
    private lateinit var sprintDateValidator: SprintDateValidator

    private val now = LocalDateTime.now()

    @Test
    fun `validate should return error when start date is after end date`() {
        val startDate = now.plusDays(2)
        val endDate = now.plusDays(1)

        val result = sprintDateValidator.validate(startDate, endDate)

        assertEquals(SprintDateValidationResult.START_DATE_AFTER_END_DATE, result)
    }

    @Test
    fun `validate should return error when sprint overlaps with existing sprint`() {
        val startDate = now.plusDays(1)
        val endDate = now.plusDays(14)
        val existingSprint = Sprint(
            id = 1L,
            startDate = now,
            endDate = now.plusDays(7)
        )

        `when`(sprintService.findAll()).thenReturn(listOf(existingSprint))

        val result = sprintDateValidator.validate(startDate, endDate)

        assertEquals(SprintDateValidationResult.OVERLAPS_WITH_EXISTING_SPRINT, result)
    }

    @Test
    fun `validate should return error when sprint duration is less than minimum`() {
        val startDate = now
        val endDate = now.plusDays(6) // Less than 1 week

        val result = sprintDateValidator.validate(startDate, endDate)

        assertEquals(SprintDateValidationResult.DURATION_TOO_SHORT, result)
    }

    @Test
    fun `validate should return error when sprint duration is more than maximum`() {
        val startDate = now
        val endDate = now.plusDays(29) // More than 4 weeks

        val result = sprintDateValidator.validate(startDate, endDate)

        assertEquals(SprintDateValidationResult.DURATION_TOO_LONG, result)
    }

    @Test
    fun `validate should return success when sprint dates are valid`() {
        val startDate = now
        val endDate = now.plusDays(14) // 2 weeks

        val result = sprintDateValidator.validate(startDate, endDate)

        assertEquals(SprintDateValidationResult.SUCCESS, result)
    }

    @Test
    fun `validateForUpdate should return error when start date is after end date`() {
        val sprintId = 1L
        val startDate = now.plusDays(2)
        val endDate = now.plusDays(1)

        val result = sprintDateValidator.validateForUpdate(sprintId, startDate, endDate)

        assertEquals(SprintDateValidationResult.START_DATE_AFTER_END_DATE, result)
    }

    @Test
    fun `validateForUpdate should return error when sprint overlaps with existing sprint`() {
        val sprintId = 1L
        val startDate = now.plusDays(1)
        val endDate = now.plusDays(14)
        val existingSprint = Sprint(
            id = 2L,
            startDate = now,
            endDate = now.plusDays(7)
        )

        `when`(sprintService.findAll()).thenReturn(listOf(existingSprint))

        val result = sprintDateValidator.validateForUpdate(sprintId, startDate, endDate)

        assertEquals(SprintDateValidationResult.OVERLAPS_WITH_EXISTING_SPRINT, result)
    }

    @Test
    fun `validateForUpdate should return success when sprint dates are valid`() {
        val sprintId = 1L
        val startDate = now
        val endDate = now.plusDays(14) // 2 weeks
        val existingSprint = Sprint(
            id = sprintId,
            startDate = now.minusDays(7),
            endDate = now.plusDays(7)
        )

        `when`(sprintService.findAll()).thenReturn(listOf(existingSprint))

        val result = sprintDateValidator.validateForUpdate(sprintId, startDate, endDate)

        assertEquals(SprintDateValidationResult.SUCCESS, result)
    }
}
