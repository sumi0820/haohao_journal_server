package com.haohao.journal.server.repository

import com.haohao.journal.server.model.SprintReview
import com.haohao.journal.server.model.Sprint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SprintReviewRepository : JpaRepository<SprintReview, Long> {
    fun findBySprint(sprint: Sprint): List<SprintReview>
}
