package com.haohao.journal.server.service

import com.haohao.journal.server.model.DailyReview
import com.haohao.journal.server.model.Sprint
import java.time.LocalDate

interface DailyReviewService {
    fun createDailyReview(
        sprint: Sprint,
        date: LocalDate,
    ): DailyReview

    fun getDailyReview(
        sprint: Sprint,
        date: LocalDate,
    ): DailyReview?

    fun updateDailyReview(
        sprint: Sprint,
        date: LocalDate,
        doneSummary: String?,
        feelingSummary: String?,
        nextDayPlan: String?,
    ): DailyReview

    fun getDailyReviewsBySprint(sprint: Sprint): List<DailyReview>

    fun findById(id: Long): DailyReview?

    fun delete(id: Long)
}
