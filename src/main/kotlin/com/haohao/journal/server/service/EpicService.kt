package com.haohao.journal.server.service

import com.haohao.journal.server.dto.EpicCreateRequest
import com.haohao.journal.server.dto.EpicUpdateRequest
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
    fun create(request: EpicCreateRequest): Epic {
        val sprint =
            sprintService.findById(request.sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: ${request.sprintId}")

        val epic =
            Epic(
                sprint = sprint,
                title = request.title,
                description = request.description,
            )
        return epicRepository.save(epic)
    }

    @Transactional
    fun update(
        id: Long,
        request: EpicUpdateRequest,
    ): Epic {
        val epic = findById(id) ?: throw IllegalArgumentException("Epic not found with id: $id")

        request.title?.let { epic.title = it }
        request.description?.let { epic.description = it }
        epic.updatedAt = LocalDateTime.now()

        return epicRepository.save(epic)
    }

    @Transactional
    fun delete(id: Long) {
        val epic = findById(id) ?: throw IllegalArgumentException("Epic not found with id: $id")
        epicRepository.delete(epic)
    }
}
