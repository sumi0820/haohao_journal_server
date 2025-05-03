package com.haohao.journal.server.service

import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.SprintStatus
import com.haohao.journal.server.repository.SprintRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

@Service
class SprintAutoGenerator(
    private val sprintService: SprintService,
    private val sprintRepository: SprintRepository,
    private val clock: Clock = Clock.systemDefaultZone(),
) {
    companion object {
        private const val START_HOUR = 0
        private const val START_MINUTE = 0
        private const val END_HOUR = 23
        private const val END_MINUTE = 59
        private val START_TIME = LocalTime.of(START_HOUR, START_MINUTE)
        private val END_TIME = LocalTime.of(END_HOUR, END_MINUTE)
        private val START_DAY = DayOfWeek.MONDAY
        private val END_DAY = DayOfWeek.SUNDAY
        private const val SPRINT_DURATION_DAYS = 6L
    }

    fun generateCurrentSprintIfNeeded() {
        val now = LocalDateTime.now(clock)
        val currentSprint = sprintRepository.findByDate(now)

        if (currentSprint == null) {
            val startDate =
                now
                    .with(TemporalAdjusters.nextOrSame(START_DAY))
                    .with(START_TIME)
            val endDate =
                startDate
                    .plusDays(SPRINT_DURATION_DAYS)
                    .with(END_TIME)

            val sprint =
                Sprint(
                    startDate = startDate,
                    endDate = endDate,
                    status = SprintStatus.NOT_STARTED,
                )

            sprintRepository.save(sprint)
        }
    }

    fun generateNextSprint() {
        val now = LocalDateTime.now(clock)
        val startDate = now.plusDays(1)
        val endDate = startDate.plusWeeks(2)
        sprintService.create(startDate, endDate)
    }
}
