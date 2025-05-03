package com.haohao.journal.server.controller

import com.haohao.journal.server.config.TestSecurityConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(HealthCheckController::class)
@Import(TestSecurityConfig::class)
class HealthCheckControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `health check endpoint should return OK`() {
        mockMvc.perform(get("/health"))
            .andExpect(status().isOk)
            .andExpect(content().string("OK"))
    }
}
