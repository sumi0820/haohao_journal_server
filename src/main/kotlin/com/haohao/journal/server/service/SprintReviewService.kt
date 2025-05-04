package com.haohao.journal.server.service

import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.SprintReview

interface SprintReviewService {
    fun createSprintReview(sprint: Sprint): SprintReview

    fun getSprintReview(sprint: Sprint): SprintReview?

    fun updateSprintReview(
        sprint: Sprint,
        doneSummary: String?,
        feelingSummary: String?,
        nextSprintPlan: String?,
    ): SprintReview

    fun findById(id: Long): SprintReview?

    fun delete(id: Long)
}
