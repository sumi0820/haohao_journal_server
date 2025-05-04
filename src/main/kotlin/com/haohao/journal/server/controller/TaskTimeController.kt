package com.haohao.journal.server.controller

import com.haohao.journal.server.model.Task
import com.haohao.journal.server.model.TaskStatus
import com.haohao.journal.server.service.TaskTimeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tasks")
class TaskTimeController(
    private val taskTimeService: TaskTimeService,
) {
    @PutMapping("/{id}/estimated-hours")
    fun updateTaskEstimatedHours(
        @PathVariable id: Long,
        @RequestParam hours: Float,
    ): ResponseEntity<Task> {
        val task = taskTimeService.updateEstimatedHours(id, hours)
        return ResponseEntity.ok(task)
    }

    @PutMapping("/{id}/actual-hours")
    fun updateTaskActualHours(
        @PathVariable id: Long,
        @RequestParam hours: Float,
    ): ResponseEntity<Task> {
        val task = taskTimeService.updateActualHours(id, hours)
        return ResponseEntity.ok(task)
    }

    @GetMapping("/sprint/{sprintId}/status/{status}/estimated-hours")
    fun getEstimatedHoursBySprintAndStatus(
        @PathVariable sprintId: Long,
        @PathVariable status: TaskStatus,
    ): ResponseEntity<Float> {
        val hours = taskTimeService.sumEstimatedHoursBySprintAndStatus(sprintId, status)
        return ResponseEntity.ok(hours)
    }

    @GetMapping("/sprint/{sprintId}/status/{status}/actual-hours")
    fun getActualHoursBySprintAndStatus(
        @PathVariable sprintId: Long,
        @PathVariable status: TaskStatus,
    ): ResponseEntity<Float> {
        val hours = taskTimeService.sumActualHoursBySprintAndStatus(sprintId, status)
        return ResponseEntity.ok(hours)
    }
}
