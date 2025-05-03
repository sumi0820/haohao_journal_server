package com.haohao.journal.server.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.haohao.journal.server.config.TestConfig
import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.SprintStatus
import com.haohao.journal.server.service.SprintService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

data class CreateSprintRequest(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
)

data class UpdateSprintRequest(
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
)

@WebMvcTest(SprintController::class)
@Import(TestConfig::class)
class SprintControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var sprintService: SprintService

    private val now = LocalDateTime.now()
    private val sprint =
        Sprint(
            id = 1L,
            startDate = now,
            endDate = now.plusWeeks(2),
            status = SprintStatus.NOT_STARTED,
        )

    @Test
    fun `should return all sprints`() {
        // Given
        val sprints = listOf(sprint)
        `when`(sprintService.findAll()).thenReturn(sprints)

        // When/Then
        mockMvc.perform(get("/api/sprints"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(sprints)))
    }

    @Test
    fun `should return sprint by id when exists`() {
        // Given
        `when`(sprintService.findById(1L)).thenReturn(sprint)

        // When/Then
        mockMvc.perform(get("/api/sprints/1"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(sprint)))
    }

    @Test
    fun `should return 404 when sprint not found`() {
        // Given
        `when`(sprintService.findById(1L)).thenReturn(null)

        // When/Then
        mockMvc.perform(get("/api/sprints/1"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should create sprint`() {
        // Given
        val request = CreateSprintRequest(startDate = now, endDate = now.plusWeeks(2))
        `when`(sprintService.create(request.startDate, request.endDate)).thenReturn(sprint)

        // When/Then
        mockMvc.perform(
            post("/api/sprints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(sprint)))
    }

    @Test
    fun `should update sprint when exists`() {
        // Given
        val request = UpdateSprintRequest(startDate = now, endDate = now.plusWeeks(2))
        `when`(sprintService.findById(1L)).thenReturn(sprint)
        `when`(sprintService.update(1L, request.startDate, request.endDate)).thenReturn(sprint)

        // When/Then
        mockMvc.perform(
            put("/api/sprints/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(sprint)))
    }

    @Test
    fun `should return 404 when updating non-existent sprint`() {
        // Given
        val request = UpdateSprintRequest(startDate = now, endDate = now.plusWeeks(2))
        `when`(sprintService.findById(1L)).thenReturn(null)

        // When/Then
        mockMvc.perform(
            put("/api/sprints/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should update sprint status when exists`() {
        // Given
        val status = SprintStatus.IN_PROGRESS
        `when`(sprintService.findById(1L)).thenReturn(sprint)
        `when`(sprintService.updateStatus(1L, status)).thenReturn(sprint)

        // When/Then
        mockMvc.perform(
            put("/api/sprints/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(status)),
        )
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(sprint)))
    }

    @Test
    fun `should return 404 when updating status of non-existent sprint`() {
        // Given
        val status = SprintStatus.IN_PROGRESS
        `when`(sprintService.findById(1L)).thenReturn(null)

        // When/Then
        mockMvc.perform(
            put("/api/sprints/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(status)),
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should delete sprint when exists`() {
        // Given
        `when`(sprintService.findById(1L)).thenReturn(sprint)

        // When/Then
        mockMvc.perform(delete("/api/sprints/1"))
            .andExpect(status().isNoContent)

        verify(sprintService).delete(1L)
    }

    @Test
    fun `should return 404 when deleting non-existent sprint`() {
        // Given
        `when`(sprintService.findById(1L)).thenReturn(null)

        // When/Then
        mockMvc.perform(delete("/api/sprints/1"))
            .andExpect(status().isNotFound)
    }
}
