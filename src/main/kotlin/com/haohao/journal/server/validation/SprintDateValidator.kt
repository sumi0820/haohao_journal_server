package com.haohao.journal.server.validation

import com.haohao.journal.server.model.Sprint
import com.haohao.journal.server.repository.SprintRepository
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SprintDateValidator(
    private val sprintRepository: SprintRepository,
) : ConstraintValidator<SprintDateConstraint, Sprint> {
    override fun initialize(constraintAnnotation: SprintDateConstraint) {
        // Initialization is not required, but needed as an interface implementation
    }

    override fun isValid(
        sprint: Sprint,
        context: ConstraintValidatorContext,
    ): Boolean {
        val violations = mutableListOf<String>()

        if (sprint.startDate.isAfter(sprint.endDate)) {
            violations.add("Start date must be before end date")
        }

        if (sprint.startDate.isBefore(LocalDateTime.now())) {
            violations.add("Start date must be in the future")
        }

        if (sprint.endDate.isBefore(LocalDateTime.now())) {
            violations.add("End date must be in the future")
        }

        if (violations.isNotEmpty()) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate(violations.joinToString(", "))
                .addConstraintViolation()
            return false
        }

        return true
    }

    fun validate(sprint: Sprint): List<String> {
        val violations = mutableListOf<String>()

        if (sprint.startDate.isAfter(sprint.endDate)) {
            violations.add("Start date must be before end date")
        }

        if (sprint.startDate.isBefore(LocalDateTime.now())) {
            violations.add("Start date must be in the future")
        }

        if (sprint.endDate.isBefore(LocalDateTime.now())) {
            violations.add("End date must be in the future")
        }

        val overlappingSprints = sprintRepository.findOverlappingSprints(sprint.startDate, sprint.endDate)
        if (overlappingSprints.isNotEmpty()) {
            violations.add("Sprint dates overlap with existing sprint")
        }

        return violations
    }

    fun validateForUpdate(
        sprint: Sprint,
        existingSprint: Sprint,
    ): List<String> {
        val violations = mutableListOf<String>()

        if (sprint.startDate.isAfter(sprint.endDate)) {
            violations.add("Start date must be before end date")
        }

        if (sprint.startDate.isBefore(LocalDateTime.now())) {
            violations.add("Start date must be in the future")
        }

        if (sprint.endDate.isBefore(LocalDateTime.now())) {
            violations.add("End date must be in the future")
        }

        val overlappingSprints =
            sprintRepository.findOverlappingSprints(sprint.startDate, sprint.endDate)
                .filter { it.id != existingSprint.id }
        if (overlappingSprints.isNotEmpty()) {
            violations.add("Sprint dates overlap with existing sprint")
        }

        return violations
    }
}
