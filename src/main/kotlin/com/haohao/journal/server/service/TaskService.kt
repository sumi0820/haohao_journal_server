package com.haohao.journal.server.service

import com.haohao.journal.server.model.Task
import com.haohao.journal.server.model.TaskStatus
import com.haohao.journal.server.repository.TaskRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val sprintService: SprintService,
    private val epicService: EpicService,
) {
    @Transactional(readOnly = true)
    fun findAllBySprint(sprintId: Long): List<Task> {
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        return taskRepository.findBySprint(sprint)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Task? = taskRepository.findById(id).orElse(null)

    @Transactional(readOnly = true)
    fun findTasksForDate(
        sprintId: Long,
        date: LocalDateTime,
    ): List<Task> {
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        val startOfDay = date.toLocalDate().atStartOfDay()
        val endOfDay = startOfDay.plusDays(1).minusNanos(1)
        return taskRepository.findBySprintAndPlannedDateBetween(sprint, startOfDay, endOfDay)
    }

    @Transactional(readOnly = true)
    fun findOverdueTasks(sprintId: Long): List<Task> {
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        return taskRepository.findBySprintAndPlannedDateBeforeAndStatusNot(
            sprint,
            LocalDateTime.now(),
            TaskStatus.DONE,
        )
    }

    @Transactional
    fun create(
        sprintId: Long,
        epicId: Long,
        title: String,
        memo: String?,
        plannedDate: LocalDateTime,
    ): Task {
        val sprint =
            sprintService.findById(sprintId)
                ?: throw IllegalArgumentException("Sprint not found with id: $sprintId")
        val epic =
            epicService.findById(epicId)
                ?: throw IllegalArgumentException("Epic not found with id: $epicId")

        val task =
            Task(
                sprint = sprint,
                epic = epic,
                title = title,
                memo = memo,
                plannedDate = plannedDate,
            )
        return taskRepository.save(task)
    }

    @Transactional
    fun update(
        id: Long,
        epicId: Long?,
        title: String?,
        memo: String?,
        plannedDate: LocalDateTime?,
        status: TaskStatus?,
    ): Task {
        val task = findById(id) ?: throw IllegalArgumentException("Task not found with id: $id")

        epicId?.let {
            val epic =
                epicService.findById(it)
                    ?: throw IllegalArgumentException("Epic not found with id: $it")
            task.epic = epic
        }

        title?.let { task.title = it }
        memo?.let { task.memo = it }
        plannedDate?.let { task.plannedDate = it }
        status?.let {
            task.status = it
            if (it == TaskStatus.DONE) {
                task.completedDate = LocalDateTime.now()
            }
        }

        task.updatedAt = LocalDateTime.now()
        return taskRepository.save(task)
    }

    @Transactional
    fun delete(id: Long) {
        val task = findById(id) ?: throw IllegalArgumentException("Task not found with id: $id")
        taskRepository.delete(task)
    }
}
