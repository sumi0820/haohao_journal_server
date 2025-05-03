package com.haohao.journal.server.service

import com.haohao.journal.server.model.SprintReview
import com.haohao.journal.server.repository.SprintReviewRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class SprintReviewService(
    private val sprintReviewRepository: SprintReviewRepository,
    private val sprintService: SprintService,
) {
    @Transactional(readOnly = true)
    fun findAllBySprint(sprintId: Long): List<SprintReview> {
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        return sprintReviewRepository.findBySprint(sprint)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): SprintReview? = sprintReviewRepository.findById(id).orElse(null)

    @Transactional
    fun create(
        sprintId: Long,
        title: String,
        content: String,
        reviewDate: LocalDateTime,
    ): SprintReview {
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")

        val review =
            SprintReview(
                sprint = sprint,
                title = title,
                content = content,
                reviewDate = reviewDate,
            )
        return sprintReviewRepository.save(review)
    }

    @Transactional
    fun update(
        id: Long,
        title: String?,
        content: String?,
        reviewDate: LocalDateTime?,
    ): SprintReview {
        val review = findById(id) ?: throw IllegalArgumentException("Review not found with id: $id")

        title?.let { review.title = it }
        content?.let { review.content = it }
        reviewDate?.let { review.reviewDate = it }
        review.updatedAt = LocalDateTime.now()

        return sprintReviewRepository.save(review)
    }

    @Transactional
    fun delete(id: Long) {
        val review = findById(id) ?: throw IllegalArgumentException("Review not found with id: $id")
        sprintReviewRepository.delete(review)
    }
}
