package com.haohao.journal.server.controller

import com.haohao.journal.server.dto.DailyReviewCreateRequest
import com.haohao.journal.server.dto.DailyReviewUpdateRequest
import com.haohao.journal.server.model.DailyReview
import com.haohao.journal.server.service.DailyReviewService
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
@RequestMapping("/api/v1/daily-reviews")
class DailyReviewController(
    private val dailyReviewService: DailyReviewService,
) {
    @GetMapping("/sprint/{sprintId}")
    fun getDailyReviewsBySprint(
        @PathVariable sprintId: Long,
    ): ResponseEntity<List<DailyReview>> {
        val reviews = dailyReviewService.findAllBySprint(sprintId)
        return ResponseEntity.ok(reviews)
    }

    @GetMapping("/sprint/{sprintId}/date/{date}")
    fun getDailyReviewByDate(
        @PathVariable sprintId: Long,
        @PathVariable date: LocalDateTime,
    ): ResponseEntity<DailyReview> {
        val review =
            dailyReviewService.findBySprintAndDate(sprintId, date)
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(review)
    }

    @GetMapping("/{id}")
    fun getDailyReview(
        @PathVariable id: Long,
    ): ResponseEntity<DailyReview> {
        val review =
            dailyReviewService.findById(id)
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(review)
    }

    @PostMapping
    fun createDailyReview(
        @RequestBody request: DailyReviewCreateRequest,
    ): ResponseEntity<DailyReview> {
        val review = dailyReviewService.create(request)
        return ResponseEntity.ok(review)
    }

    @PutMapping("/{id}")
    fun updateDailyReview(
        @PathVariable id: Long,
        @RequestBody request: DailyReviewUpdateRequest,
    ): ResponseEntity<DailyReview> {
        val review = dailyReviewService.update(id, request)
        return ResponseEntity.ok(review)
    }

    @DeleteMapping("/{id}")
    fun deleteDailyReview(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        dailyReviewService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
