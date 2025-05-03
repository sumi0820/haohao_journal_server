package com.haohao.journal.server.repository

import com.haohao.journal.server.model.DailyReview
import com.haohao.journal.server.model.Sprint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface DailyReviewRepository : JpaRepository<DailyReview, Long> {
    fun findBySprint(sprint: Sprint): List<DailyReview>
    fun findBySprintAndReviewDateBetween(sprint: Sprint, startDate: LocalDateTime, endDate: LocalDateTime): DailyReview?
}
