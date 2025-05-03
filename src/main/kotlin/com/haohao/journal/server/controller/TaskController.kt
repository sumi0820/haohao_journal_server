package com.haohao.journal.server.controller

import com.haohao.journal.server.dto.TaskCreateRequest
import com.haohao.journal.server.dto.TaskUpdateRequest
import com.haohao.journal.server.model.Task
import com.haohao.journal.server.service.TaskService
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
@RequestMapping("/api/v1/tasks")
class TaskController(
    private val taskService: TaskService,
) {
    @GetMapping("/sprint/{sprintId}")
    fun getTasksBySprint(
        @PathVariable sprintId: Long,
    ): ResponseEntity<List<Task>> {
        val tasks = taskService.findAllBySprint(sprintId)
        return ResponseEntity.ok(tasks)
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

    @GetMapping("/sprint/{sprintId}/date/{date}")
    fun getTasksForDate(
        @PathVariable sprintId: Long,
        @PathVariable date: LocalDateTime,
    ): ResponseEntity<List<Task>> {
        val tasks = taskService.findTasksForDate(sprintId, date)
        return ResponseEntity.ok(tasks)
    }

    @GetMapping("/sprint/{sprintId}/overdue")
    fun getOverdueTasks(
        @PathVariable sprintId: Long,
    ): ResponseEntity<List<Task>> {
        val tasks = taskService.findOverdueTasks(sprintId)
        return ResponseEntity.ok(tasks)
    }

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
}
