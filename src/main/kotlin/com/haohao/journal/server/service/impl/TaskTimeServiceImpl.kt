package com.haohao.journal.server.service.impl

import com.haohao.journal.server.model.Task
import com.haohao.journal.server.model.TaskStatus
import com.haohao.journal.server.repository.TaskRepository
import com.haohao.journal.server.service.SprintService
import com.haohao.journal.server.service.TaskTimeService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TaskTimeServiceImpl(
    private val taskRepository: TaskRepository,
    private val sprintService: SprintService,
) : TaskTimeService {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun updateEstimatedHours(
        id: Long,
        hours: Float,
    ): Task {
        logger.info("Updating task estimated hours: {} to {}", id, hours)
        val task =
            taskRepository.findById(id)
                .orElseThrow { IllegalArgumentException("Task not found with id: $id") }
        task.estimatedHours = hours
        return taskRepository.save(task)
    }

    override fun updateActualHours(
        id: Long,
        hours: Float,
    ): Task {
        logger.info("Updating task actual hours: {} to {}", id, hours)
        val task =
            taskRepository.findById(id)
                .orElseThrow { IllegalArgumentException("Task not found with id: $id") }
        task.actualHours = hours
        return taskRepository.save(task)
    }

    override fun sumEstimatedHoursBySprintAndStatus(
        sprintId: Long,
        status: TaskStatus,
    ): Float {
        logger.info("Summing estimated hours by sprint: {} and status: {}", sprintId, status)
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        return taskRepository.sumEstimatedHoursBySprintAndStatus(sprint, status)
    }

    override fun sumActualHoursBySprintAndStatus(
        sprintId: Long,
        status: TaskStatus,
    ): Float {
        logger.info("Summing actual hours by sprint: {} and status: {}", sprintId, status)
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        return taskRepository.sumActualHoursBySprintAndStatus(sprint, status)
    }
}
