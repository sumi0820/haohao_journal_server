package com.haohao.journal.server.service

import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.SprintReview
import com.haohao.journal.server.repository.SprintReviewRepository
import com.haohao.journal.server.service.impl.SprintReviewServiceImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.Optional

class SprintReviewServiceTest {
    private val sprintReviewRepository: SprintReviewRepository = mock()
    private val service = SprintReviewServiceImpl(sprintReviewRepository)

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
        whenever(sprintReviewRepository.findBySprint(sprint)).thenReturn(sprintReview)

        val result = service.getSprintReview(sprint)

        assertEquals(sprintReview, result)
    }

    @Test
    fun `getSprintReview should return null when not found`() {
        whenever(sprintReviewRepository.findBySprint(sprint)).thenReturn(null)

        val result = service.getSprintReview(sprint)

        assertNull(result)
    }

    @Test
    fun `createSprintReview should create and return review`() {
        whenever(sprintReviewRepository.findBySprint(sprint)).thenReturn(null)
        whenever(sprintReviewRepository.save(any<SprintReview>())).thenReturn(sprintReview)

        val result = service.createSprintReview(sprint)

        assertEquals(sprintReview, result)
    }

    @Test
    fun `createSprintReview should throw exception when review already exists`() {
        whenever(sprintReviewRepository.findBySprint(sprint)).thenReturn(sprintReview)

        assertThrows<IllegalStateException> {
            service.createSprintReview(sprint)
        }
    }

    @Test
    fun `updateSprintReview should update and return review`() {
        whenever(sprintReviewRepository.findBySprint(sprint)).thenReturn(sprintReview)
        whenever(sprintReviewRepository.save(any<SprintReview>())).thenReturn(sprintReview)

        val result =
            service.updateSprintReview(
                sprint = sprint,
                doneSummary = "Updated done summary",
                feelingSummary = "Updated feeling summary",
                nextSprintPlan = "Updated next sprint plan",
            )

        assertEquals(sprintReview, result)
        assertEquals("Updated done summary", result.doneSummary)
        assertEquals("Updated feeling summary", result.feelingSummary)
        assertEquals("Updated next sprint plan", result.nextSprintPlan)
    }

    @Test
    fun `updateSprintReview should throw exception when review not found`() {
        whenever(sprintReviewRepository.findBySprint(sprint)).thenReturn(null)

        assertThrows<IllegalArgumentException> {
            service.updateSprintReview(
                sprint = sprint,
                doneSummary = "Updated done summary",
                feelingSummary = "Updated feeling summary",
                nextSprintPlan = "Updated next sprint plan",
            )
        }
    }

    @Test
    fun `findById should return review when found`() {
        whenever(sprintReviewRepository.findById(1L)).thenReturn(Optional.of(sprintReview))

        val result = service.findById(1L)

        assertEquals(sprintReview, result)
    }

    @Test
    fun `findById should return null when not found`() {
        whenever(sprintReviewRepository.findById(1L)).thenReturn(Optional.empty())

        val result = service.findById(1L)

        assertNull(result)
    }

    @Test
    fun `delete should delete review`() {
        whenever(sprintReviewRepository.findById(1L)).thenReturn(Optional.of(sprintReview))

        service.delete(1L)

        verify(sprintReviewRepository).delete(sprintReview)
    }

    @Test
    fun `delete should throw exception when review not found`() {
        whenever(sprintReviewRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<IllegalArgumentException> {
            service.delete(1L)
        }
    }
}
