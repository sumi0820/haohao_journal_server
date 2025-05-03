package com.haohao.journal.server.controller

import com.haohao.journal.server.model.Task
import com.haohao.journal.server.model.TaskStatus
import com.haohao.journal.server.service.TaskService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/tasks")
class TaskController(
    private val taskService: TaskService,
) {
    @GetMapping("/sprint/{sprintId}")
    fun getTasksBySprint(@PathVariable sprintId: Long): ResponseEntity<List<Task>> {
        val tasks = taskService.findAllBySprint(sprintId)
        return ResponseEntity.ok(tasks)
    }

    @GetMapping("/{id}")
    fun getTask(@PathVariable id: Long): ResponseEntity<Task> {
        val task = taskService.findById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(task)
    }

    @GetMapping("/sprint/{sprintId}/date/{date}")
    fun getTasksForDate(
        @PathVariable sprintId: Long,
        @PathVariable date: LocalDateTime,
    ): ResponseEntity<List<Task>> {
        val tasks = taskService.findTasksForDate(sprintId, date)
        return ResponseEntity.ok(tasks)
    }

    @GetMapping("/sprint/{sprintId}/overdue")
    fun getOverdueTasks(@PathVariable sprintId: Long): ResponseEntity<List<Task>> {
        val tasks = taskService.findOverdueTasks(sprintId)
        return ResponseEntity.ok(tasks)
    }

    @PostMapping
    fun createTask(
        @RequestParam sprintId: Long,
        @RequestParam epicId: Long,
        @RequestParam title: String,
        @RequestParam(required = false) memo: String?,
        @RequestParam plannedDate: LocalDateTime,
    ): ResponseEntity<Task> {
        val task = taskService.create(sprintId, epicId, title, memo, plannedDate)
        return ResponseEntity.ok(task)
    }

    @PutMapping("/{id}")
    fun updateTask(
        @PathVariable id: Long,
        @RequestParam(required = false) epicId: Long?,
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) memo: String?,
        @RequestParam(required = false) plannedDate: LocalDateTime?,
        @RequestParam(required = false) status: TaskStatus?,
    ): ResponseEntity<Task> {
        val task = taskService.update(id, epicId, title, memo, plannedDate, status)
        return ResponseEntity.ok(task)
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<Void> {
        taskService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
