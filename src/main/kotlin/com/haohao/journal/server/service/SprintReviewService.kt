package com.haohao.journal.server.service

import com.haohao.journal.server.dto.SprintReviewCreateRequest
import com.haohao.journal.server.dto.SprintReviewUpdateRequest
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
        val sprint = sprintService.findById(sprintId)
            ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        return sprintReviewRepository.findBySprint(sprint)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): SprintReview? = sprintReviewRepository.findById(id).orElse(null)

    @Transactional(readOnly = true)
    fun findBySprintAndDate(sprintId: Long, date: LocalDateTime): SprintReview? {
        val sprint = sprintService.findById(sprintId)
            ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        return sprintReviewRepository.findBySprint(sprint)
            .firstOrNull { it.reviewDate.toLocalDate() == date.toLocalDate() }
    }

    @Transactional
    fun create(request: SprintReviewCreateRequest): SprintReview {
        val sprint = sprintService.findById(request.sprintId)
            ?: throw IllegalArgumentException("Sprint not found with id: ${request.sprintId}")

        // スプリントレビューは1スプリントにつき1つまでしか作成できない
        val existingReview = findBySprintAndDate(request.sprintId, request.reviewDate)
        check(existingReview == null) { "Sprint review already exists for sprint: ${request.sprintId}" }

        val sprintReview = SprintReview(
            sprint = sprint,
            title = request.title,
            content = request.content,
            reviewDate = request.reviewDate,
        )
        return sprintReviewRepository.save(sprintReview)
    }

    @Transactional
    fun update(
        id: Long,
        request: SprintReviewUpdateRequest,
    ): SprintReview {
        val sprintReview = findById(id)
            ?: throw IllegalArgumentException("SprintReview not found with id: $id")

        request.title?.let { sprintReview.title = it }
        request.content?.let { sprintReview.content = it }
        sprintReview.updatedAt = LocalDateTime.now()

        return sprintReviewRepository.save(sprintReview)
    }

    @Transactional
    fun delete(id: Long) {
        val sprintReview = findById(id)
            ?: throw IllegalArgumentException("SprintReview not found with id: $id")
        sprintReviewRepository.delete(sprintReview)
    }
}
