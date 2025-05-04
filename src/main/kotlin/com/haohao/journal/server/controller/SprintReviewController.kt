package com.haohao.journal.server.controller

import com.haohao.journal.server.dto.SprintReviewCreateRequest
import com.haohao.journal.server.dto.SprintReviewUpdateRequest
import com.haohao.journal.server.model.SprintReview
import com.haohao.journal.server.service.SprintReviewService
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

@RestController
@RequestMapping("/api/v1/sprint-reviews")
class SprintReviewController(
    private val sprintReviewService: SprintReviewService,
) {
    @GetMapping("/sprint/{sprintId}")
    fun getSprintReviewsBySprint(
        @PathVariable sprintId: Long,
    ): ResponseEntity<List<SprintReview>> {
        val reviews = sprintReviewService.findAllBySprint(sprintId)
        return ResponseEntity.ok(reviews)
    }

    @GetMapping("/sprint/{sprintId}/date/{date}")
    fun getSprintReviewByDate(
        @PathVariable sprintId: Long,
        @PathVariable date: LocalDateTime,
    ): ResponseEntity<SprintReview> {
        val review =
            sprintReviewService.findBySprintAndDate(sprintId, date)
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(review)
    }

    @GetMapping("/{id}")
    fun getSprintReview(
        @PathVariable id: Long,
    ): ResponseEntity<SprintReview> {
        val review =
            sprintReviewService.findById(id)
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(review)
    }

    @PostMapping
    fun createSprintReview(
        @RequestBody request: SprintReviewCreateRequest,
    ): ResponseEntity<SprintReview> {
        val review = sprintReviewService.create(request)
        return ResponseEntity.ok(review)
    }

    @PutMapping("/{id}")
    fun updateSprintReview(
        @PathVariable id: Long,
        @RequestBody request: SprintReviewUpdateRequest,
    ): ResponseEntity<SprintReview> {
        val review = sprintReviewService.update(id, request)
        return ResponseEntity.ok(review)
    }

    @DeleteMapping("/{id}")
    fun deleteSprintReview(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        sprintReviewService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
