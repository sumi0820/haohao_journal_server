package com.haohao.journal.server.service.impl

import com.haohao.journal.server.dto.TaskCreateRequest
import com.haohao.journal.server.dto.TaskUpdateRequest
import com.haohao.journal.server.model.Task
import com.haohao.journal.server.model.TaskPriority
import com.haohao.journal.server.model.TaskStatus
import com.haohao.journal.server.repository.TaskRepository
import com.haohao.journal.server.service.EpicService
import com.haohao.journal.server.service.SprintService
import com.haohao.journal.server.service.TaskService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class TaskServiceImpl(
    private val taskRepository: TaskRepository,
    private val sprintService: SprintService,
    private val epicService: EpicService,
) : TaskService {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun create(request: TaskCreateRequest): Task {
        logger.info("Creating task: {}", request)
        val sprint =
            sprintService.findById(request.sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: ${request.sprintId}")
        val epic =
            epicService.findById(request.epicId)
                ?: throw IllegalArgumentException("Epic not found with id: ${request.epicId}")

        val task =
            Task(
                sprint = sprint,
                epic = epic,
                title = request.title,
                memo = request.memo,
                plannedDate = request.plannedDate,
            )

        return taskRepository.save(task)
    }

    override fun update(
        id: Long,
        request: TaskUpdateRequest,
    ): Task {
        logger.info("Updating task: {} with request: {}", id, request)
        val task =
            taskRepository.findById(id)
                .orElseThrow { IllegalArgumentException("Task not found with id: $id") }

        request.title?.let { task.title = it }
        request.memo?.let { task.memo = it }
        request.plannedDate?.let { task.plannedDate = it }
        request.status?.let { task.status = it }

        if (request.status == TaskStatus.DONE) {
            task.completedDate = LocalDateTime.now()
        }

        return taskRepository.save(task)
    }

    override fun delete(id: Long) {
        logger.info("Deleting task: {}", id)
        val task =
            taskRepository.findById(id)
                .orElseThrow { IllegalArgumentException("Task not found with id: $id") }
        taskRepository.delete(task)
    }

    override fun findById(id: Long): Task? {
        logger.info("Finding task by id: {}", id)
        return taskRepository.findById(id).orElse(null)
    }

    override fun findAllBySprint(sprintId: Long): List<Task> {
        logger.info("Finding all tasks by sprint: {}", sprintId)
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        return taskRepository.findBySprint(sprint)
    }

    override fun findBySprintAndDateBetween(
        sprintId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<Task> {
        logger.info("Finding tasks by sprint: {} and date between {} and {}", sprintId, startDate, endDate)
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        return taskRepository.findBySprintAndPlannedDateBetween(sprint, startDate, endDate)
    }

    override fun findBySprintAndStatus(
        sprintId: Long,
        status: TaskStatus,
    ): List<Task> {
        logger.info("Finding tasks by sprint: {} and status: {}", sprintId, status)
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        return taskRepository.findBySprintAndStatus(sprint, status)
    }

    override fun findBySprintAndPriority(
        sprintId: Long,
        priority: TaskPriority,
    ): List<Task> {
        logger.info("Finding tasks by sprint: {} and priority: {}", sprintId, priority)
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        return taskRepository.findBySprintAndPriority(sprint, priority)
    }

    override fun findBySprintAndStatusAndPriorityOrderByPlannedDateAsc(
        sprintId: Long,
        status: TaskStatus,
        priority: TaskPriority,
    ): List<Task> {
        logger.info("Finding tasks by sprint: {}, status: {}, and priority: {}", sprintId, status, priority)
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        return taskRepository.findBySprintAndStatusAndPriorityOrderByPlannedDateAsc(sprint, status, priority)
    }

    override fun updatePriority(
        id: Long,
        priority: TaskPriority,
    ): Task {
        logger.info("Updating task priority: {} to {}", id, priority)
        val task =
            taskRepository.findById(id)
                .orElseThrow { IllegalArgumentException("Task not found with id: $id") }
        task.priority = priority
        return taskRepository.save(task)
    }
}
