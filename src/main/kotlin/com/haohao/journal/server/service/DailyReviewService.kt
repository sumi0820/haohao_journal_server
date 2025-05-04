package com.haohao.journal.server.service

import com.haohao.journal.server.dto.DailyReviewCreateRequest
import com.haohao.journal.server.dto.DailyReviewUpdateRequest
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
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        return dailyReviewRepository.findBySprint(sprint)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): DailyReview? = dailyReviewRepository.findById(id).orElse(null)

    @Transactional(readOnly = true)
    fun findBySprintAndDate(
        sprintId: Long,
        date: LocalDateTime,
    ): DailyReview? {
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        val startOfDay = date.toLocalDate().atStartOfDay()
        val endOfDay = startOfDay.plusDays(1).minusNanos(1)
        return dailyReviewRepository.findBySprintAndReviewDateBetween(sprint, startOfDay, endOfDay)
    }

    @Transactional
    fun create(request: DailyReviewCreateRequest): DailyReview {
        val sprint =
            sprintService.findById(request.sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: ${request.sprintId}")

        val dailyReview =
            DailyReview(
                sprint = sprint,
                title = request.title,
                content = request.content,
                reviewDate = request.reviewDate,
            )
        return dailyReviewRepository.save(dailyReview)
    }

    @Transactional
    fun update(
        id: Long,
        request: DailyReviewUpdateRequest,
    ): DailyReview {
        val dailyReview =
            findById(id)
                ?: throw IllegalArgumentException("DailyReview not found with id: $id")

        request.title?.let { dailyReview.title = it }
        request.content?.let { dailyReview.content = it }
        dailyReview.updatedAt = LocalDateTime.now()

        return dailyReviewRepository.save(dailyReview)
    }

    @Transactional
    fun delete(id: Long) {
        val dailyReview =
            findById(id)
                ?: throw IllegalArgumentException("DailyReview not found with id: $id")
        dailyReviewRepository.delete(dailyReview)
    }
}
