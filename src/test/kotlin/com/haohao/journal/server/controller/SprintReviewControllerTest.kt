package com.haohao.journal.server.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.haohao.journal.server.config.TestConfig
import com.haohao.journal.server.dto.SprintReviewCreateRequest
import com.haohao.journal.server.dto.SprintReviewUpdateRequest
import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.SprintReview
import com.haohao.journal.server.service.SprintReviewService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@WebMvcTest(SprintReviewController::class)
@Import(TestConfig::class)
class SprintReviewControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var sprintReviewService: SprintReviewService

    private val now = LocalDateTime.now()
    private val sprint = Sprint(id = 1L, startDate = now, endDate = now.plusDays(7))
    private val sprintReview =
        SprintReview(
            id = 1L,
            sprint = sprint,
            title = "Test Sprint Review",
            content = "Test Content",
            reviewDate = now,
        )

    @Test
    fun `getSprintReviewsBySprint should return list of reviews`() {
        whenever(sprintReviewService.findAllBySprint(1L))
            .thenReturn(listOf(sprintReview))

        mockMvc.perform(get("/api/v1/sprint-reviews/sprint/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(sprintReview.id))
            .andExpect(jsonPath("$[0].title").value(sprintReview.title))
            .andExpect(jsonPath("$[0].content").value(sprintReview.content))
    }

    @Test
    fun `getSprintReviewsBySprint should return empty list when no reviews exist`() {
        whenever(sprintReviewService.findAllBySprint(1L))
            .thenReturn(emptyList())

        mockMvc.perform(get("/api/v1/sprint-reviews/sprint/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun `getSprintReviewsBySprint should return 400 when sprint not found`() {
        whenever(sprintReviewService.findAllBySprint(1L))
            .thenThrow(IllegalArgumentException("Sprint not found with id: 1"))

        mockMvc.perform(get("/api/v1/sprint-reviews/sprint/1"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `getSprintReviewByDate should return review when exists`() {
        whenever(sprintReviewService.findBySprintAndDate(1L, now))
            .thenReturn(sprintReview)

        mockMvc.perform(
            get("/api/v1/sprint-reviews/sprint/1/date/{date}", now.format(DateTimeFormatter.ISO_DATE_TIME)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sprintReview.id))
            .andExpect(jsonPath("$.title").value(sprintReview.title))
            .andExpect(jsonPath("$.content").value(sprintReview.content))
    }

    @Test
    fun `getSprintReviewByDate should return 404 when not found`() {
        whenever(sprintReviewService.findBySprintAndDate(1L, now))
            .thenReturn(null)

        mockMvc.perform(
            get("/api/v1/sprint-reviews/sprint/1/date/{date}", now.format(DateTimeFormatter.ISO_DATE_TIME)),
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `getSprintReviewByDate should return 400 when sprint not found`() {
        whenever(sprintReviewService.findBySprintAndDate(1L, now))
            .thenThrow(IllegalArgumentException("Sprint not found with id: 1"))

        mockMvc.perform(
            get("/api/v1/sprint-reviews/sprint/1/date/{date}", now.format(DateTimeFormatter.ISO_DATE_TIME)),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `getSprintReview should return review when exists`() {
        whenever(sprintReviewService.findById(1L))
            .thenReturn(sprintReview)

        mockMvc.perform(get("/api/v1/sprint-reviews/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sprintReview.id))
            .andExpect(jsonPath("$.title").value(sprintReview.title))
            .andExpect(jsonPath("$.content").value(sprintReview.content))
    }

    @Test
    fun `getSprintReview should return 404 when not found`() {
        whenever(sprintReviewService.findById(1L))
            .thenReturn(null)

        mockMvc.perform(get("/api/v1/sprint-reviews/1"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `createSprintReview should create and return review`() {
        val request =
            SprintReviewCreateRequest(
                sprintId = 1L,
                title = "Test Sprint Review",
                content = "Test Content",
                reviewDate = now,
            )

        whenever(sprintReviewService.create(any()))
            .thenReturn(sprintReview)

        mockMvc.perform(
            post("/api/v1/sprint-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sprintReview.id))
            .andExpect(jsonPath("$.title").value(sprintReview.title))
            .andExpect(jsonPath("$.content").value(sprintReview.content))
    }

    @Test
    fun `createSprintReview should return 400 when sprint not found`() {
        val request =
            SprintReviewCreateRequest(
                sprintId = 1L,
                title = "Test Sprint Review",
                content = "Test Content",
                reviewDate = now,
            )

        whenever(sprintReviewService.create(any()))
            .thenThrow(IllegalArgumentException("Sprint not found with id: 1"))

        mockMvc.perform(
            post("/api/v1/sprint-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `createSprintReview should return 400 when review already exists`() {
        val request =
            SprintReviewCreateRequest(
                sprintId = 1L,
                title = "Test Sprint Review",
                content = "Test Content",
                reviewDate = now,
            )

        whenever(sprintReviewService.create(any()))
            .thenThrow(IllegalStateException("Sprint review already exists for sprint: 1"))

        mockMvc.perform(
            post("/api/v1/sprint-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `updateSprintReview should update and return review`() {
        val request =
            SprintReviewUpdateRequest(
                title = "Updated Sprint Review",
                content = "Updated Content",
            )

        val updatedReview =
            sprintReview.copy(
                title = "Updated Sprint Review",
                content = "Updated Content",
            )

        whenever(sprintReviewService.update(1L, request))
            .thenReturn(updatedReview)

        mockMvc.perform(
            put("/api/v1/sprint-reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(updatedReview.id))
            .andExpect(jsonPath("$.title").value(updatedReview.title))
            .andExpect(jsonPath("$.content").value(updatedReview.content))
    }

    @Test
    fun `updateSprintReview should return 404 when review not found`() {
        val request =
            SprintReviewUpdateRequest(
                title = "Updated Sprint Review",
                content = "Updated Content",
            )

        whenever(sprintReviewService.update(1L, request))
            .thenThrow(IllegalArgumentException("SprintReview not found with id: 1"))

        mockMvc.perform(
            put("/api/v1/sprint-reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `deleteSprintReview should return no content`() {
        mockMvc.perform(delete("/api/v1/sprint-reviews/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `deleteSprintReview should return 404 when review not found`() {
        doThrow(IllegalArgumentException("SprintReview not found with id: 1"))
            .whenever(sprintReviewService).delete(1L)

        mockMvc.perform(delete("/api/v1/sprint-reviews/1"))
            .andExpect(status().isNotFound)
    }
}
