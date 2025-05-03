package com.haohao.journal.server.service

import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.SprintStatus
import com.haohao.journal.server.repository.SprintRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class SprintService(
    private val sprintRepository: SprintRepository
) {
    @Transactional(readOnly = true)
    fun findAll(): List<Sprint> = sprintRepository.findAll()

    @Transactional(readOnly = true)
    fun findById(id: Long): Sprint? = sprintRepository.findById(id).orElse(null)

    @Transactional(readOnly = true)
    fun findByDate(date: LocalDateTime): Sprint? = sprintRepository.findByDate(date)

    @Transactional
    fun create(startDate: LocalDateTime, endDate: LocalDateTime): Sprint {
        val sprint = Sprint(startDate = startDate, endDate = endDate)
        return sprintRepository.save(sprint)
    }

    @Transactional
    fun update(id: Long, startDate: LocalDateTime?, endDate: LocalDateTime?): Sprint {
        val sprint = findById(id) ?: throw IllegalArgumentException("Sprint not found with id: $id")
        return sprintRepository.save(
            sprint.copy(
                startDate = startDate ?: sprint.startDate,
                endDate = endDate ?: sprint.endDate
            )
        )
    }

    @Transactional
    fun updateStatus(id: Long, status: SprintStatus): Sprint {
        val sprint = findById(id) ?: throw IllegalArgumentException("Sprint not found with id: $id")
        return sprintRepository.save(sprint.copy(status = status))
    }

    @Transactional
    fun delete(id: Long) {
        val sprint = findById(id) ?: throw IllegalArgumentException("Sprint not found with id: $id")
        sprintRepository.delete(sprint)
    }
}
