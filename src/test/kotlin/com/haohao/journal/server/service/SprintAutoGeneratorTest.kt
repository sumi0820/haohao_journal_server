package com.haohao.journal.server.service

import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.SprintStatus
import com.haohao.journal.server.repository.SprintRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.argThat
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

@ExtendWith(MockitoExtension::class)
class SprintAutoGeneratorTest {
    @Mock
    private lateinit var sprintService: SprintService

    @Mock
    private lateinit var sprintRepository: SprintRepository

    @Mock
    private lateinit var clock: Clock

    private lateinit var sprintAutoGenerator: SprintAutoGenerator

    private lateinit var fixedNow: LocalDateTime
    private lateinit var fixedClock: Clock
    private val zoneId = ZoneId.systemDefault()

    @BeforeEach
    fun setUp() {
        fixedNow = LocalDateTime.of(2024, 5, 1, 0, 0)
        fixedClock = Clock.fixed(fixedNow.atZone(zoneId).toInstant(), zoneId)
        sprintAutoGenerator = SprintAutoGenerator(sprintService, sprintRepository, fixedClock)
    }

    @Test
    fun `generateCurrentSprintIfNeeded should create new sprint when no current sprint exists`() {
        // Given
        `when`(sprintRepository.findByDate(fixedNow)).thenReturn(null)

        val startDate = fixedNow.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)).with(LocalTime.of(0, 0))
        val endDate = startDate.plusDays(6).with(LocalTime.of(23, 59))

        // When
        sprintAutoGenerator.generateCurrentSprintIfNeeded()

        // Then
        verify(sprintRepository).findByDate(fixedNow)
        verify(sprintRepository).save(argThat { sprint ->
            sprint.startDate == startDate &&
            sprint.endDate == endDate &&
            sprint.status == SprintStatus.NOT_STARTED
        })
    }

    @Test
    fun `generateCurrentSprintIfNeeded should not create new sprint when current sprint exists`() {
        // Given
        val existingSprint = Sprint(startDate = fixedNow, endDate = fixedNow.plusWeeks(2))
        `when`(sprintRepository.findByDate(fixedNow)).thenReturn(existingSprint)

        // When
        sprintAutoGenerator.generateCurrentSprintIfNeeded()

        // Then
        verify(sprintRepository).findByDate(fixedNow)
        verify(sprintRepository, never()).save(any())
    }

    @Test
    fun `generateNextSprint should create new sprint with correct dates`() {
        // Given
        val startDate = fixedNow.plusDays(1)
        val endDate = startDate.plusWeeks(2)
        val expectedSprint = Sprint(startDate = startDate, endDate = endDate)

        `when`(sprintService.create(startDate, endDate)).thenReturn(expectedSprint)

        // When
        sprintAutoGenerator.generateNextSprint()

        // Then
        verify(sprintService).create(startDate, endDate)
    }
}
