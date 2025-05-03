package com.haohao.journal.server.dto

import com.haohao.journal.server.model.TaskStatus
import java.time.LocalDateTime

data class TaskUpdateRequest(
    val epicId: Long? = null,
    val title: String? = null,
    val memo: String? = null,
    val plannedDate: LocalDateTime? = null,
    val status: TaskStatus? = null,
)
