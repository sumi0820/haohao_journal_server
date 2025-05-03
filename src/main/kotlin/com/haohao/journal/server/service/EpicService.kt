package com.haohao.journal.server.service

import com.haohao.journal.server.model.Epic
import com.haohao.journal.server.repository.EpicRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class EpicService(
    private val epicRepository: EpicRepository,
    private val sprintService: SprintService,
) {
    @Transactional(readOnly = true)
    fun findAllBySprint(sprintId: Long): List<Epic> {
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        return epicRepository.findBySprint(sprint)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Epic? = epicRepository.findById(id).orElse(null)

    @Transactional
    fun create(
        sprintId: Long,
        title: String,
        description: String?,
    ): Epic {
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")

        val epic =
            Epic(
                sprint = sprint,
                title = title,
                description = description,
            )
        return epicRepository.save(epic)
    }

    @Transactional
    fun update(
        id: Long,
        title: String?,
        description: String?,
    ): Epic {
        val epic = findById(id) ?: throw IllegalArgumentException("Epic not found with id: $id")

        title?.let { epic.title = it }
        description?.let { epic.description = it }
        epic.updatedAt = LocalDateTime.now()

        return epicRepository.save(epic)
    }

    @Transactional
    fun delete(id: Long) {
        val epic = findById(id) ?: throw IllegalArgumentException("Epic not found with id: $id")
        epicRepository.delete(epic)
    }
}
