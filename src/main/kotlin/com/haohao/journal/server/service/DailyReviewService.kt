package com.haohao.journal.server.service

import com.haohao.journal.server.model.DailyReview
import com.haohao.journal.server.repository.DailyReviewRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class DailyReviewService(
    private val dailyReviewRepository: DailyReviewRepository,
    private val sprintService: SprintService,
) {
    @Transactional(readOnly = true)
    fun findAllBySprint(sprintId: Long): List<DailyReview> {
        val sprint = sprintService.findById(sprintId)
        require(sprint != null) { "Sprint not found with id: $sprintId" }
        return dailyReviewRepository.findBySprint(sprint)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): DailyReview? = dailyReviewRepository.findById(id).orElse(null)

    @Transactional(readOnly = true)
    fun findBySprintAndDate(
        sprintId: Long,
        date: LocalDateTime,
    ): DailyReview? {
        val sprint = sprintService.findById(sprintId)
        require(sprint != null) { "Sprint not found with id: $sprintId" }
        val startOfDay = date.toLocalDate().atStartOfDay()
        val endOfDay = startOfDay.plusDays(1).minusNanos(1)
        return dailyReviewRepository.findBySprintAndReviewDateBetween(sprint, startOfDay, endOfDay)
    }

    @Transactional
    fun create(
        sprintId: Long,
        title: String,
        content: String,
        reviewDate: LocalDateTime,
    ): DailyReview {
        val sprint = sprintService.findById(sprintId)
        require(sprint != null) { "Sprint not found with id: $sprintId" }

        val existingReview = findBySprintAndDate(sprintId, reviewDate)
        require(existingReview == null) { "Daily review already exists for date: $reviewDate" }

        val review =
            DailyReview(
                sprint = sprint,
                title = title,
                content = content,
                reviewDate = reviewDate,
            )
        return dailyReviewRepository.save(review)
    }

    @Transactional
    fun update(
        id: Long,
        title: String?,
        content: String?,
        reviewDate: LocalDateTime?,
    ): DailyReview {
        val review = findById(id)
        require(review != null) { "Review not found with id: $id" }

        title?.let { review.title = it }
        content?.let { review.content = it }
        reviewDate?.let { review.reviewDate = it }
        review.updatedAt = LocalDateTime.now()

        return dailyReviewRepository.save(review)
    }

    @Transactional
    fun delete(id: Long) {
        val review = findById(id)
        require(review != null) { "Review not found with id: $id" }
        dailyReviewRepository.delete(review)
    }
}
