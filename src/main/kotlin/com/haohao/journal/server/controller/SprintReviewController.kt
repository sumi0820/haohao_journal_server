package com.haohao.journal.server.controller

import com.haohao.journal.server.dto.SprintReviewCreateRequest
import com.haohao.journal.server.dto.SprintReviewUpdateRequest
import com.haohao.journal.server.model.SprintReview
import com.haohao.journal.server.service.SprintReviewService
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

@RestController
@RequestMapping("/api/v1/sprint-reviews")
class SprintReviewController(
    private val sprintReviewService: SprintReviewService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/sprint/{sprintId}")
    fun getSprintReviewsBySprint(
        @PathVariable sprintId: Long,
    ): ResponseEntity<List<SprintReview>> {
        return try {
            val reviews = sprintReviewService.findAllBySprint(sprintId)
            ResponseEntity.ok(reviews)
        } catch (e: IllegalArgumentException) {
            logger.warn("Failed to get sprint reviews: {}", e.message, e)
            ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/sprint/{sprintId}/date/{date}")
    fun getSprintReviewByDate(
        @PathVariable sprintId: Long,
        @PathVariable date: LocalDateTime,
    ): ResponseEntity<SprintReview> {
        return try {
            val review =
                sprintReviewService.findBySprintAndDate(sprintId, date)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(review)
        } catch (e: IllegalArgumentException) {
            logger.warn("Failed to get sprint review by date: {}", e.message, e)
            ResponseEntity.badRequest().build()
        }
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
        return try {
            val review = sprintReviewService.create(request)
            ResponseEntity.ok(review)
        } catch (e: IllegalArgumentException) {
            logger.warn("Failed to create sprint review: {}", e.message, e)
            ResponseEntity.badRequest().build()
        } catch (e: IllegalStateException) {
            logger.warn("Failed to create sprint review: {}", e.message, e)
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/{id}")
    fun updateSprintReview(
        @PathVariable id: Long,
        @RequestBody request: SprintReviewUpdateRequest,
    ): ResponseEntity<SprintReview> {
        return try {
            val review = sprintReviewService.update(id, request)
            ResponseEntity.ok(review)
        } catch (e: IllegalArgumentException) {
            logger.warn("Failed to update sprint review: {}", e.message, e)
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteSprintReview(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        return try {
            sprintReviewService.delete(id)
            ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            logger.warn("Failed to delete sprint review: {}", e.message, e)
            ResponseEntity.notFound().build()
        }
    }
}
