package com.haohao.journal.server.repository

import com.haohao.journal.server.model.Sprint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface SprintRepository : JpaRepository<Sprint, Long> {
    @Query("SELECT s FROM Sprint s WHERE :date BETWEEN s.startDate AND s.endDate")
    fun findByDate(
        @Param("date") date: LocalDateTime,
    ): Sprint?
}
