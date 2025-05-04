package com.haohao.journal.server.controller

import com.haohao.journal.server.model.SprintReview
import com.haohao.journal.server.service.SprintReviewService
import com.haohao.journal.server.service.SprintService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sprint-reviews")
class SprintReviewController(
    private val sprintReviewService: SprintReviewService,
    private val sprintService: SprintService,
) {
    @GetMapping("/sprint/{sprintId}")
    fun getSprintReview(
        @PathVariable sprintId: Long,
    ): ResponseEntity<SprintReview> {
        val sprint = sprintService.findById(sprintId) ?: return ResponseEntity.notFound().build()
        val review = sprintReviewService.getSprintReview(sprint)
        return if (review != null) ResponseEntity.ok(review) else ResponseEntity.notFound().build()
    }

    @PostMapping("/sprint/{sprintId}")
    fun createSprintReview(
        @PathVariable sprintId: Long,
    ): ResponseEntity<SprintReview> {
        val sprint =
            sprintService.findById(sprintId)
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(sprintReviewService.createSprintReview(sprint))
    }

    @PutMapping("/sprint/{sprintId}")
    fun updateSprintReview(
        @PathVariable sprintId: Long,
        @RequestParam(required = false) doneSummary: String?,
        @RequestParam(required = false) feelingSummary: String?,
        @RequestParam(required = false) nextSprintPlan: String?,
    ): ResponseEntity<SprintReview> {
        val sprint =
            sprintService.findById(sprintId)
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(
            sprintReviewService.updateSprintReview(
                sprint = sprint,
                doneSummary = doneSummary,
                feelingSummary = feelingSummary,
                nextSprintPlan = nextSprintPlan,
            ),
        )
    }

    @DeleteMapping("/{id}")
    fun deleteSprintReview(
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        sprintReviewService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
