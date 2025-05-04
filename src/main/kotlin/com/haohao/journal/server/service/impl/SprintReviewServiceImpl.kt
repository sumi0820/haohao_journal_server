package com.haohao.journal.server.service.impl

import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.SprintReview
import com.haohao.journal.server.repository.SprintReviewRepository
import com.haohao.journal.server.service.SprintReviewService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class SprintReviewServiceImpl(
    private val sprintReviewRepository: SprintReviewRepository,
) : SprintReviewService {
    @Transactional
    override fun createSprintReview(sprint: Sprint): SprintReview {
        val existingReview = sprintReviewRepository.findBySprint(sprint)
        check(existingReview == null) { "Sprint review already exists for sprint: ${sprint.id}" }

        val sprintReview =
            SprintReview(
                sprint = sprint,
            )
        return sprintReviewRepository.save(sprintReview)
    }

    @Transactional(readOnly = true)
    override fun getSprintReview(sprint: Sprint): SprintReview? {
        return sprintReviewRepository.findBySprint(sprint)
    }

    @Transactional
    override fun updateSprintReview(
        sprint: Sprint,
        doneSummary: String?,
        feelingSummary: String?,
        nextSprintPlan: String?,
    ): SprintReview {
        val sprintReview =
            getSprintReview(sprint)
                ?: throw IllegalArgumentException("Sprint review not found for sprint ${sprint.id}")

        sprintReview.apply {
            this.doneSummary = doneSummary
            this.feelingSummary = feelingSummary
            this.nextSprintPlan = nextSprintPlan
            this.updatedAt = LocalDateTime.now()
        }

        return sprintReviewRepository.save(sprintReview)
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): SprintReview? {
        return sprintReviewRepository.findById(id).orElse(null)
    }

    @Transactional
    override fun delete(id: Long) {
        val sprintReview =
            findById(id)
                ?: throw IllegalArgumentException("SprintReview not found with id: $id")
        sprintReviewRepository.delete(sprintReview)
    }
}
