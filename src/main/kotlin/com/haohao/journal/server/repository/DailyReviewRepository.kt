package com.haohao.journal.server.repository

import com.haohao.journal.server.model.DailyReview
import com.haohao.journal.server.model.Sprint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface DailyReviewRepository : JpaRepository<DailyReview, Long> {
    fun findBySprintAndDate(
        sprint: Sprint,
        date: LocalDate,
    ): DailyReview?

    fun findBySprint(sprint: Sprint): List<DailyReview>
}
