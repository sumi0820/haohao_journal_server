package com.haohao.journal.server.controller

import com.haohao.journal.server.model.DailyReview
import com.haohao.journal.server.service.DailyReviewService
import com.haohao.journal.server.service.SprintService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/daily-reviews")
class DailyReviewController(
    private val dailyReviewService: DailyReviewService,
    private val sprintService: SprintService,
) {
    @GetMapping("/sprint/{sprintId}")
    fun getDailyReviewsBySprint(
        @PathVariable sprintId: Long,
    ): ResponseEntity<List<DailyReview>> {
        val sprint =
            sprintService.findById(sprintId)
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(dailyReviewService.getDailyReviewsBySprint(sprint))
    }

    @GetMapping("/sprint/{sprintId}/date/{date}")
    fun getDailyReviewBySprintAndDate(
        @PathVariable sprintId: Long,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
    ): ResponseEntity<DailyReview> {
        val sprint = sprintService.findById(sprintId) ?: return ResponseEntity.notFound().build()
        val dailyReview = dailyReviewService.getDailyReview(sprint, date)
        return if (dailyReview != null) ResponseEntity.ok(dailyReview) else ResponseEntity.notFound().build()
    }

    @PostMapping("/sprint/{sprintId}/date/{date}")
    fun createDailyReview(
        @PathVariable sprintId: Long,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
    ): ResponseEntity<DailyReview> {
        val sprint =
            sprintService.findById(sprintId)
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(dailyReviewService.createDailyReview(sprint, date))
    }

    @PutMapping("/sprint/{sprintId}/date/{date}")
    fun updateDailyReview(
        @PathVariable sprintId: Long,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
        @RequestParam(required = false) doneSummary: String?,
        @RequestParam(required = false) feelingSummary: String?,
        @RequestParam(required = false) nextDayPlan: String?,
    ): ResponseEntity<DailyReview> {
        val sprint =
            sprintService.findById(sprintId)
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(
            dailyReviewService.updateDailyReview(
                sprint = sprint,
                date = date,
                doneSummary = doneSummary,
                feelingSummary = feelingSummary,
                nextDayPlan = nextDayPlan,
            ),
        )
    }

    @DeleteMapping("/{id}")
    fun deleteDailyReview(
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        dailyReviewService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
