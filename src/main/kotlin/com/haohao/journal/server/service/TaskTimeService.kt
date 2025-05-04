package com.haohao.journal.server.service

import com.haohao.journal.server.model.Task
import com.haohao.journal.server.model.TaskStatus

interface TaskTimeService {
    fun updateEstimatedHours(
        id: Long,
        hours: Float,
    ): Task

    fun updateActualHours(
        id: Long,
        hours: Float,
    ): Task

    fun sumEstimatedHoursBySprintAndStatus(
        sprintId: Long,
        status: TaskStatus,
    ): Float

    fun sumActualHoursBySprintAndStatus(
        sprintId: Long,
        status: TaskStatus,
    ): Float
}
