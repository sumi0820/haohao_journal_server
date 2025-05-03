package com.haohao.journal.server.validation

import com.haohao.journal.server.model.Sprint
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

object SprintDateValidationRules {
    private const val START_HOUR = 0
    private const val START_MINUTE = 0
    private const val END_HOUR = 23
    private const val END_MINUTE = 59
    private val START_TIME = LocalTime.of(START_HOUR, START_MINUTE)
    private val END_TIME = LocalTime.of(END_HOUR, END_MINUTE)
    private val START_DAY = DayOfWeek.MONDAY
    private val END_DAY = DayOfWeek.SUNDAY
    private const val SPRINT_DURATION_DAYS = 6L

    fun isNewSprint(sprint: Sprint): Boolean {
        return sprint.id == 0L
    }

    fun isStartDateInPast(sprint: Sprint): Boolean {
        return sprint.startDate.isBefore(LocalDateTime.now())
    }

    fun isStartDateBeforeEndDate(sprint: Sprint): Boolean {
        return !sprint.startDate.isAfter(sprint.endDate)
    }

    fun isValidDuration(sprint: Sprint): Boolean {
        val startDateTime = sprint.startDate.with(START_TIME)
        val endDateTime = sprint.endDate.with(END_TIME)

        // 開始日が月曜日0:00であることを確認
        val isStartMonday =
            startDateTime.dayOfWeek == START_DAY &&
                startDateTime.toLocalTime() == START_TIME

        // 終了日が日曜日23:59であることを確認
        val isSundayEnd =
            endDateTime.dayOfWeek == END_DAY &&
                endDateTime.toLocalTime() == END_TIME

        // 開始日と終了日が同じ週であることを確認
        val expectedEndDate =
            startDateTime
                .with(TemporalAdjusters.nextOrSame(START_DAY))
                .plusDays(SPRINT_DURATION_DAYS)
                .with(END_TIME)
        val isValidWeek = endDateTime.isEqual(expectedEndDate)

        return isStartMonday && isSundayEnd && isValidWeek
    }
}
