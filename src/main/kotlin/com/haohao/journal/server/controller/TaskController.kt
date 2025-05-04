package com.haohao.journal.server.controller

import com.haohao.journal.server.dto.TaskCreateRequest
import com.haohao.journal.server.dto.TaskUpdateRequest
import com.haohao.journal.server.model.Task
import com.haohao.journal.server.model.TaskPriority
import com.haohao.journal.server.model.TaskStatus
import com.haohao.journal.server.service.TaskService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/tasks")
class TaskController(
    private val taskService: TaskService,
) {
    @PostMapping
    fun createTask(
        @RequestBody request: TaskCreateRequest,
    ): ResponseEntity<Task> {
        val task = taskService.create(request)
        return ResponseEntity.ok(task)
    }

    @PutMapping("/{id}")
    fun updateTask(
        @PathVariable id: Long,
        @RequestBody request: TaskUpdateRequest,
    ): ResponseEntity<Task> {
        val task = taskService.update(id, request)
        return ResponseEntity.ok(task)
    }

    @DeleteMapping("/{id}")
    fun deleteTask(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        taskService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    fun getTask(
        @PathVariable id: Long,
    ): ResponseEntity<Task> {
        val task =
            taskService.findById(id)
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(task)
    }

    @GetMapping("/sprint/{sprintId}")
    fun getTasksBySprint(
        @PathVariable sprintId: Long,
    ): ResponseEntity<List<Task>> {
        val tasks = taskService.findAllBySprint(sprintId)
        return ResponseEntity.ok(tasks)
    }

    @GetMapping("/sprint/{sprintId}/date")
    fun getTasksBySprintAndDate(
        @PathVariable sprintId: Long,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime,
    ): ResponseEntity<List<Task>> {
        val tasks = taskService.findBySprintAndDateBetween(sprintId, startDate, endDate)
        return ResponseEntity.ok(tasks)
    }

    @GetMapping("/sprint/{sprintId}/status/{status}")
    fun getTasksBySprintAndStatus(
        @PathVariable sprintId: Long,
        @PathVariable status: TaskStatus,
    ): ResponseEntity<List<Task>> {
        val tasks = taskService.findBySprintAndStatus(sprintId, status)
        return ResponseEntity.ok(tasks)
    }

    @GetMapping("/sprint/{sprintId}/priority/{priority}")
    fun getTasksBySprintAndPriority(
        @PathVariable sprintId: Long,
        @PathVariable priority: TaskPriority,
    ): ResponseEntity<List<Task>> {
        val tasks = taskService.findBySprintAndPriority(sprintId, priority)
        return ResponseEntity.ok(tasks)
    }

    @GetMapping("/sprint/{sprintId}/status/{status}/priority/{priority}")
    fun getTasksBySprintAndStatusAndPriority(
        @PathVariable sprintId: Long,
        @PathVariable status: TaskStatus,
        @PathVariable priority: TaskPriority,
    ): ResponseEntity<List<Task>> {
        val tasks = taskService.findBySprintAndStatusAndPriorityOrderByPlannedDateAsc(sprintId, status, priority)
        return ResponseEntity.ok(tasks)
    }

    @PutMapping("/{id}/priority")
    fun updateTaskPriority(
        @PathVariable id: Long,
        @RequestParam priority: TaskPriority,
    ): ResponseEntity<Task> {
        val task = taskService.updatePriority(id, priority)
        return ResponseEntity.ok(task)
    }
}
