package com.haohao.journal.server.controller

import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.SprintStatus
import com.haohao.journal.server.service.SprintService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

data class CreateSprintRequest(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
)

data class UpdateSprintRequest(
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
)

@RestController
@RequestMapping("/api/sprints")
class SprintController(
    private val sprintService: SprintService,
) {
    private val logger = LoggerFactory.getLogger(SprintController::class.java)

    @GetMapping
    fun getAllSprints(): ResponseEntity<List<Sprint>> {
        return ResponseEntity.ok(sprintService.findAll())
    }

    @GetMapping("/{id}")
    fun getSprint(
        @PathVariable id: Long,
    ): ResponseEntity<Sprint> {
        return sprintService.findById(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createSprint(
        @RequestBody request: CreateSprintRequest,
    ): ResponseEntity<Sprint> {
        return ResponseEntity.ok(sprintService.create(request.startDate, request.endDate))
    }

    @PutMapping("/{id}")
    fun updateSprint(
        @PathVariable id: Long,
        @RequestBody request: UpdateSprintRequest,
    ): ResponseEntity<Sprint> {
        if (sprintService.findById(id) == null) {
            return ResponseEntity.notFound().build()
        }

        return try {
            ResponseEntity.ok(sprintService.update(id, request.startDate, request.endDate))
        } catch (e: IllegalArgumentException) {
            logger.warn("Failed to update sprint: ${e.message}")
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/{id}/status")
    fun updateSprintStatus(
        @PathVariable id: Long,
        @RequestBody status: SprintStatus,
    ): ResponseEntity<Sprint> {
        if (sprintService.findById(id) == null) {
            return ResponseEntity.notFound().build()
        }

        return try {
            ResponseEntity.ok(sprintService.updateStatus(id, status))
        } catch (e: IllegalArgumentException) {
            logger.warn("Failed to update sprint status: ${e.message}")
            ResponseEntity.badRequest().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteSprint(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        if (sprintService.findById(id) == null) {
            return ResponseEntity.notFound().build()
        }

        return try {
            sprintService.delete(id)
            ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            logger.warn("Failed to delete sprint: ${e.message}")
            ResponseEntity.badRequest().build()
        }
    }
}
