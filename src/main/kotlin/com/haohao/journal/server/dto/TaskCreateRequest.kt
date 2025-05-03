package com.haohao.journal.server.dto

import java.time.LocalDateTime

data class TaskCreateRequest(
    val sprintId: Long,
    val epicId: Long,
    val title: String,
    val memo: String? = null,
    val plannedDate: LocalDateTime,
)
