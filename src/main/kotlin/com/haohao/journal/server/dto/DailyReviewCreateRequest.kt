package com.haohao.journal.server.dto

import java.time.LocalDateTime

data class DailyReviewCreateRequest(
    val sprintId: Long,
    val title: String,
    val content: String,
    val reviewDate: LocalDateTime,
)
