package com.haohao.journal.server.service

import com.haohao.journal.server.dto.SprintReviewCreateRequest
import com.haohao.journal.server.dto.SprintReviewUpdateRequest
import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.model.SprintReview
import com.haohao.journal.server.repository.SprintReviewRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class SprintReviewServiceTest {
    @Mock
    private lateinit var sprintReviewRepository: SprintReviewRepository

    @Mock
    private lateinit var sprintService: SprintService

    @InjectMocks
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
    fun `findAllBySprint should return list of reviews when sprint exists`() {
        `when`(sprintService.findById(1L)).thenReturn(sprint)
        `when`(sprintReviewRepository.findBySprint(sprint)).thenReturn(listOf(sprintReview))

        val result = sprintReviewService.findAllBySprint(1L)

        assertEquals(listOf(sprintReview), result)
    }

    @Test
    fun `findAllBySprint should throw exception when sprint not found`() {
        `when`(sprintService.findById(1L)).thenReturn(null)

        assertThrows<IllegalArgumentException> {
            sprintReviewService.findAllBySprint(1L)
        }
    }

    @Test
    fun `findById should return review when exists`() {
        `when`(sprintReviewRepository.findById(1L)).thenReturn(Optional.of(sprintReview))

        val result = sprintReviewService.findById(1L)

        assertEquals(sprintReview, result)
    }

    @Test
    fun `findById should return null when not exists`() {
        `when`(sprintReviewRepository.findById(1L)).thenReturn(Optional.empty())

        val result = sprintReviewService.findById(1L)

        assertNull(result)
    }

    @Test
    fun `findBySprintAndDate should return review when exists`() {
        `when`(sprintService.findById(1L)).thenReturn(sprint)
        `when`(sprintReviewRepository.findBySprint(sprint)).thenReturn(listOf(sprintReview))

        val result = sprintReviewService.findBySprintAndDate(1L, now)

        assertEquals(sprintReview, result)
    }

    @Test
    fun `findBySprintAndDate should return null when no review exists for date`() {
        `when`(sprintService.findById(1L)).thenReturn(sprint)
        `when`(sprintReviewRepository.findBySprint(sprint)).thenReturn(emptyList())

        val result = sprintReviewService.findBySprintAndDate(1L, now)

        assertNull(result)
    }

    @Test
    fun `findBySprintAndDate should throw exception when sprint not found`() {
        `when`(sprintService.findById(1L)).thenReturn(null)

        assertThrows<IllegalArgumentException> {
            sprintReviewService.findBySprintAndDate(1L, now)
        }
    }

    @Test
    fun `create should return created review`() {
        val request =
            SprintReviewCreateRequest(
                sprintId = 1L,
                title = "Test Sprint Review",
                content = "Test Content",
                reviewDate = now,
            )

        `when`(sprintService.findById(1L)).thenReturn(sprint)
        `when`(sprintReviewRepository.findBySprint(sprint)).thenReturn(emptyList())
        `when`(sprintReviewRepository.save(any())).thenReturn(sprintReview)

        val result = sprintReviewService.create(request)

        assertEquals(sprintReview, result)
    }

    @Test
    fun `create should throw exception when sprint not found`() {
        val request =
            SprintReviewCreateRequest(
                sprintId = 1L,
                title = "Test Sprint Review",
                content = "Test Content",
                reviewDate = now,
            )

        `when`(sprintService.findById(1L)).thenReturn(null)

        assertThrows<IllegalArgumentException> {
            sprintReviewService.create(request)
        }
    }

    @Test
    fun `create should throw exception when review already exists for date`() {
        val request =
            SprintReviewCreateRequest(
                sprintId = 1L,
                title = "Test Sprint Review",
                content = "Test Content",
                reviewDate = now,
            )

        `when`(sprintService.findById(1L)).thenReturn(sprint)
        `when`(sprintReviewRepository.findBySprint(sprint)).thenReturn(listOf(sprintReview))

        assertThrows<IllegalStateException> {
            sprintReviewService.create(request)
        }
    }

    @Test
    fun `update should return updated review`() {
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

        `when`(sprintReviewRepository.findById(1L)).thenReturn(Optional.of(sprintReview))
        `when`(sprintReviewRepository.save(any())).thenReturn(updatedReview)

        val result = sprintReviewService.update(1L, request)

        assertEquals(updatedReview, result)
    }

    @Test
    fun `update should throw exception when review not found`() {
        val request =
            SprintReviewUpdateRequest(
                title = "Updated Sprint Review",
                content = "Updated Content",
            )

        `when`(sprintReviewRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<IllegalArgumentException> {
            sprintReviewService.update(1L, request)
        }
    }

    @Test
    fun `delete should delete review when exists`() {
        `when`(sprintReviewRepository.findById(1L)).thenReturn(Optional.of(sprintReview))

        sprintReviewService.delete(1L)

        verify(sprintReviewRepository).delete(sprintReview)
    }

    @Test
    fun `delete should throw exception when review not found`() {
        `when`(sprintReviewRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<IllegalArgumentException> {
            sprintReviewService.delete(1L)
        }
    }
}
