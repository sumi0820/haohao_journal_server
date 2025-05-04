package com.haohao.journal.server.controller

import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.SprintReview
import com.haohao.journal.server.service.SprintReviewService
import com.haohao.journal.server.service.SprintService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@WebMvcTest(SprintReviewController::class)
class SprintReviewControllerTest {
    @MockBean
    private lateinit var sprintReviewService: SprintReviewService

    @MockBean
    private lateinit var sprintService: SprintService

    @Autowired
    private lateinit var controller: SprintReviewController

    private val now = LocalDateTime.now()
    private val sprint = Sprint(id = 1L, startDate = now, endDate = now.plusDays(7))
    private val sprintReview =
        SprintReview(
            id = 1L,
            sprint = sprint,
            doneSummary = "Done summary",
            feelingSummary = "Feeling summary",
            nextSprintPlan = "Next sprint plan",
        )

    @Test
    fun `getSprintReview should return review when found`() {
        whenever(sprintService.findById(1L)).thenReturn(sprint)
        whenever(sprintReviewService.getSprintReview(sprint)).thenReturn(sprintReview)

        val response = controller.getSprintReview(1L)

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == sprintReview)
    }

    @Test
    fun `getSprintReview should return not found when sprint not found`() {
        whenever(sprintService.findById(1L)).thenReturn(null)

        val response = controller.getSprintReview(1L)

        assert(response.statusCode == HttpStatus.NOT_FOUND)
    }

    @Test
    fun `getSprintReview should return not found when review not found`() {
        whenever(sprintService.findById(1L)).thenReturn(sprint)
        whenever(sprintReviewService.getSprintReview(sprint)).thenReturn(null)

        val response = controller.getSprintReview(1L)

        assert(response.statusCode == HttpStatus.NOT_FOUND)
    }

    @Test
    fun `createSprintReview should create and return review`() {
        whenever(sprintService.findById(1L)).thenReturn(sprint)
        whenever(sprintReviewService.createSprintReview(sprint)).thenReturn(sprintReview)

        val response = controller.createSprintReview(1L)

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == sprintReview)
    }

    @Test
    fun `createSprintReview should return not found when sprint not found`() {
        whenever(sprintService.findById(1L)).thenReturn(null)

        val response = controller.createSprintReview(1L)

        assert(response.statusCode == HttpStatus.NOT_FOUND)
    }

    @Test
    fun `updateSprintReview should update and return review`() {
        whenever(sprintService.findById(1L)).thenReturn(sprint)
        whenever(
            sprintReviewService.updateSprintReview(
                sprint = sprint,
                doneSummary = "Updated done summary",
                feelingSummary = "Updated feeling summary",
                nextSprintPlan = "Updated next sprint plan",
            ),
        ).thenReturn(sprintReview)

        val response = controller.updateSprintReview(
            sprintId = 1L,
            doneSummary = "Updated done summary",
            feelingSummary = "Updated feeling summary",
            nextSprintPlan = "Updated next sprint plan",
        )

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == sprintReview)
    }

    @Test
    fun `updateSprintReview should return not found when sprint not found`() {
        whenever(sprintService.findById(1L)).thenReturn(null)

        val response = controller.updateSprintReview(
            sprintId = 1L,
            doneSummary = "Updated done summary",
            feelingSummary = "Updated feeling summary",
            nextSprintPlan = "Updated next sprint plan",
        )

        assert(response.statusCode == HttpStatus.NOT_FOUND)
    }

    @Test
    fun `deleteSprintReview should delete review`() {
        val response = controller.deleteSprintReview(1L)

        verify(sprintReviewService).delete(1L)
        assert(response.statusCode == HttpStatus.NO_CONTENT)
    }
}
