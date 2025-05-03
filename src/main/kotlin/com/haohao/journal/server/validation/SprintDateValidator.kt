package com.haohao.journal.server.validation

import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.service.SprintService
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Component
class SprintDateValidator(
    private val sprintService: SprintService,
) : ConstraintValidator<SprintDateConstraint, Sprint> {
    companion object {
        private const val MIN_SPRINT_DURATION_DAYS = 7L // 1 week
        private const val MAX_SPRINT_DURATION_DAYS = 28L // 4 weeks
    }

    override fun initialize(constraintAnnotation: SprintDateConstraint) {
        // 初期化は不要ですが、インターフェースの実装として必要です
    }

    override fun isValid(
        sprint: Sprint?,
        context: ConstraintValidatorContext,
    ): Boolean {
        if (sprint == null) {
            return true
        }

        if (sprint.id == 0L && sprint.startDate.isBefore(LocalDateTime.now())) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate("Start date must not be in the past")
                .addConstraintViolation()
            return false
        }

        if (sprint.startDate.isAfter(sprint.endDate)) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate("Start date must be before end date")
                .addConstraintViolation()
            return false
        }

        val duration = ChronoUnit.DAYS.between(sprint.startDate, sprint.endDate)
        if (duration < MIN_SPRINT_DURATION_DAYS || duration > MAX_SPRINT_DURATION_DAYS) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate("Sprint duration must be between 1 day and 4 weeks")
                .addConstraintViolation()
            return false
        }

        return true
    }

    fun validate(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): SprintDateValidationResult {
        if (startDate.isAfter(endDate)) {
            return SprintDateValidationResult.START_DATE_AFTER_END_DATE
        }

        val duration = ChronoUnit.DAYS.between(startDate, endDate)
        if (duration < MIN_SPRINT_DURATION_DAYS) {
            return SprintDateValidationResult.DURATION_TOO_SHORT
        }
        if (duration > MAX_SPRINT_DURATION_DAYS) {
            return SprintDateValidationResult.DURATION_TOO_LONG
        }

        val existingSprints = sprintService.findAll()
        if (existingSprints.any { it.overlaps(startDate, endDate) }) {
            return SprintDateValidationResult.OVERLAPS_WITH_EXISTING_SPRINT
        }

        return SprintDateValidationResult.SUCCESS
    }

    fun validateForUpdate(
        sprintId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): SprintDateValidationResult {
        if (startDate.isAfter(endDate)) {
            return SprintDateValidationResult.START_DATE_AFTER_END_DATE
        }

        val duration = ChronoUnit.DAYS.between(startDate, endDate)
        if (duration < MIN_SPRINT_DURATION_DAYS) {
            return SprintDateValidationResult.DURATION_TOO_SHORT
        }
        if (duration > MAX_SPRINT_DURATION_DAYS) {
            return SprintDateValidationResult.DURATION_TOO_LONG
        }

        val existingSprints = sprintService.findAll()
        if (existingSprints.any { it.id != sprintId && it.overlaps(startDate, endDate) }) {
            return SprintDateValidationResult.OVERLAPS_WITH_EXISTING_SPRINT
        }

        return SprintDateValidationResult.SUCCESS
    }
}
