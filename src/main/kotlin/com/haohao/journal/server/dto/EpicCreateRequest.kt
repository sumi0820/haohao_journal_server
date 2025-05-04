package com.haohao.journal.server.dto

data class EpicCreateRequest(
    val sprintId: Long,
    val title: String,
    val description: String? = null,
)
