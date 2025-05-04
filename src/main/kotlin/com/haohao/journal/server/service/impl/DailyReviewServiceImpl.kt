package com.haohao.journal.server.service.impl

import com.haohao.journal.server.model.DailyReview
import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.repository.DailyReviewRepository
import com.haohao.journal.server.service.DailyReviewService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class DailyReviewServiceImpl(
    private val dailyReviewRepository: DailyReviewRepository,
) : DailyReviewService {
    @Transactional
    override fun createDailyReview(
        sprint: Sprint,
        date: LocalDate,
    ): DailyReview {
        val dailyReview =
            DailyReview(
                sprint = sprint,
                date = date,
            )
        return dailyReviewRepository.save(dailyReview)
    }

    @Transactional(readOnly = true)
    override fun getDailyReview(
        sprint: Sprint,
        date: LocalDate,
    ): DailyReview? {
        return dailyReviewRepository.findBySprintAndDate(sprint, date)
    }

    @Transactional
    override fun updateDailyReview(
        sprint: Sprint,
        date: LocalDate,
        doneSummary: String?,
        feelingSummary: String?,
        nextDayPlan: String?,
    ): DailyReview {
        val dailyReview =
            getDailyReview(sprint, date)
                ?: throw IllegalArgumentException("Daily review not found for sprint ${sprint.id} and date $date")

        dailyReview.apply {
            this.doneSummary = doneSummary
            this.feelingSummary = feelingSummary
            this.nextDayPlan = nextDayPlan
            this.updatedAt = LocalDateTime.now()
        }

        return dailyReviewRepository.save(dailyReview)
    }

    @Transactional(readOnly = true)
    override fun getDailyReviewsBySprint(sprint: Sprint): List<DailyReview> {
        return dailyReviewRepository.findBySprint(sprint)
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): DailyReview? {
        return dailyReviewRepository.findById(id).orElse(null)
    }

    @Transactional
    override fun delete(id: Long) {
        val dailyReview =
            findById(id)
                ?: throw IllegalArgumentException("DailyReview not found with id: $id")
        dailyReviewRepository.delete(dailyReview)
    }
}
