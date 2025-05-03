package com.haohao.journal.server.validation

enum class SprintDateValidationResult {
    SUCCESS,
    START_DATE_AFTER_END_DATE,
    DURATION_TOO_SHORT,
    DURATION_TOO_LONG,
    OVERLAPS_WITH_EXISTING_SPRINT
}
