package com.haohao.journal.server.config

import com.haohao.journal.server.repository.DailyReviewRepository
import com.haohao.journal.server.repository.EpicRepository
import com.haohao.journal.server.repository.SprintRepository
import com.haohao.journal.server.repository.SprintReviewRepository
import com.haohao.journal.server.repository.TaskRepository
import com.haohao.journal.server.service.SprintService
import com.haohao.journal.server.validation.SprintDateValidator
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestRepositoryConfig {
    @Bean
    @Primary
    fun sprintRepository(): SprintRepository = Mockito.mock(SprintRepository::class.java)

    @Bean
    @Primary
    fun sprintService(): SprintService = Mockito.mock(SprintService::class.java)

    @Bean
    @Primary
    fun sprintDateValidator(): SprintDateValidator = Mockito.mock(SprintDateValidator::class.java)

    @Bean
    @Primary
    fun dailyReviewRepository(): DailyReviewRepository = Mockito.mock(DailyReviewRepository::class.java)

    @Bean
    @Primary
    fun taskRepository(): TaskRepository = Mockito.mock(TaskRepository::class.java)

    @Bean
    @Primary
    fun epicRepository(): EpicRepository = Mockito.mock(EpicRepository::class.java)

    @Bean
    @Primary
    fun sprintReviewRepository(): SprintReviewRepository = Mockito.mock(SprintReviewRepository::class.java)
}
