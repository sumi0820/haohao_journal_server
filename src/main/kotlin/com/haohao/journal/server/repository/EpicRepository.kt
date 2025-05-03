package com.haohao.journal.server.repository

import com.haohao.journal.server.model.Epic
import com.haohao.journal.server.model.Sprint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EpicRepository : JpaRepository<Epic, Long> {
    fun findBySprint(sprint: Sprint): List<Epic>
}
