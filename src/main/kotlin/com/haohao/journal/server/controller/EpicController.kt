package com.haohao.journal.server.controller

import com.haohao.journal.server.dto.EpicCreateRequest
import com.haohao.journal.server.dto.EpicUpdateRequest
import com.haohao.journal.server.model.Epic
import com.haohao.journal.server.service.EpicService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/epics")
class EpicController(
    private val epicService: EpicService,
) {
    @GetMapping("/sprint/{sprintId}")
    fun getEpicsBySprint(
        @PathVariable sprintId: Long,
    ): ResponseEntity<List<Epic>> {
        val epics = epicService.findAllBySprint(sprintId)
        return ResponseEntity.ok(epics)
    }

    @GetMapping("/{id}")
    fun getEpic(
        @PathVariable id: Long,
    ): ResponseEntity<Epic> {
        val epic = epicService.findById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(epic)
    }

    @PostMapping
    fun createEpic(
        @RequestBody request: EpicCreateRequest,
    ): ResponseEntity<Epic> {
        val epic = epicService.create(request)
        return ResponseEntity.ok(epic)
    }

    @PutMapping("/{id}")
    fun updateEpic(
        @PathVariable id: Long,
        @RequestBody request: EpicUpdateRequest,
    ): ResponseEntity<Epic> {
        val epic = epicService.update(id, request)
        return ResponseEntity.ok(epic)
    }

    @DeleteMapping("/{id}")
    fun deleteEpic(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        epicService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
