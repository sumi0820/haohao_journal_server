package com.haohao.journal.server.validation

import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.repository.SprintRepository
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
    private lateinit var sprintRepository: SprintRepository

    @InjectMocks
    private lateinit var sprintDateValidator: SprintDateValidator

    private val now = LocalDateTime.now()

    @Test
    fun `validate should return error when start date is after end date`() {
        val sprint = Sprint(startDate = now.plusDays(2), endDate = now.plusDays(1))

        val result = sprintDateValidator.validate(sprint)

        assertEquals(listOf("Start date must be before end date"), result)
    }

    @Test
    fun `validate should return error when sprint overlaps with existing sprint`() {
        val sprint = Sprint(startDate = now.plusDays(1), endDate = now.plusDays(14))
        val existingSprint = Sprint(startDate = now, endDate = now.plusDays(7))

        `when`(sprintRepository.findOverlappingSprints(sprint.startDate, sprint.endDate))
            .thenReturn(listOf(existingSprint))

        val result = sprintDateValidator.validate(sprint)

        assertEquals(listOf("Sprint dates overlap with existing sprint"), result)
    }

    @Test
    fun `validate should return error when start date is in the past`() {
        val sprint = Sprint(startDate = now.minusDays(1), endDate = now.plusDays(6))

        val result = sprintDateValidator.validate(sprint)

        assertEquals(listOf("Start date must be in the future"), result)
    }

    @Test
    fun `validate should return error when end date is in the past`() {
        val sprint = Sprint(startDate = now.minusDays(2), endDate = now.minusDays(1))

        val result = sprintDateValidator.validate(sprint)

        assertEquals(
            listOf(
                "Start date must be in the future",
                "End date must be in the future",
            ),
            result,
        )
    }

    @Test
    fun `validate should return success when sprint dates are valid`() {
        val sprint = Sprint(startDate = now.plusDays(1), endDate = now.plusDays(14))

        `when`(sprintRepository.findOverlappingSprints(sprint.startDate, sprint.endDate))
            .thenReturn(emptyList())

        val result = sprintDateValidator.validate(sprint)

        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun `validateForUpdate should return error when start date is after end date`() {
        val sprint = Sprint(startDate = now.plusDays(2), endDate = now.plusDays(1))
        val existingSprint = Sprint(id = 1L, startDate = now, endDate = now.plusDays(7))

        val result = sprintDateValidator.validateForUpdate(sprint, existingSprint)

        assertEquals(listOf("Start date must be before end date"), result)
    }

    @Test
    fun `validateForUpdate should return error when sprint overlaps with existing sprint`() {
        val sprint = Sprint(startDate = now.plusDays(1), endDate = now.plusDays(14))
        val existingSprint = Sprint(id = 1L, startDate = now.minusDays(7), endDate = now)
        val overlappingSprint = Sprint(id = 2L, startDate = now, endDate = now.plusDays(7))

        `when`(sprintRepository.findOverlappingSprints(sprint.startDate, sprint.endDate))
            .thenReturn(listOf(overlappingSprint))

        val result = sprintDateValidator.validateForUpdate(sprint, existingSprint)

        assertEquals(listOf("Sprint dates overlap with existing sprint"), result)
    }

    @Test
    fun `validateForUpdate should return success when sprint dates are valid`() {
        val sprint = Sprint(startDate = now.plusDays(1), endDate = now.plusDays(14))
        val existingSprint = Sprint(id = 1L, startDate = now.minusDays(7), endDate = now)

        `when`(sprintRepository.findOverlappingSprints(sprint.startDate, sprint.endDate))
            .thenReturn(emptyList())

        val result = sprintDateValidator.validateForUpdate(sprint, existingSprint)

        assertEquals(emptyList<String>(), result)
    }
}
