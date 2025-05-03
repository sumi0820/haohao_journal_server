package com.haohao.journal.server.scheduler

import com.haohao.journal.server.service.SprintAutoGenerator
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SprintAutoGenerationScheduler(
    private val sprintAutoGenerator: SprintAutoGenerator,
) {
    @Scheduled(fixedRate = 3600000) // 1時間ごと
    fun checkAndGenerateSprint() {
        sprintAutoGenerator.generateCurrentSprintIfNeeded()
    }
}
