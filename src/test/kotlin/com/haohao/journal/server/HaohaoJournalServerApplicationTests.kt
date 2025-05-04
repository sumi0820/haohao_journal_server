package com.haohao.journal.server

import com.haohao.journal.server.config.TestConfig
import com.haohao.journal.server.config.TestRepositoryConfig
import com.haohao.journal.server.config.TestSecurityConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    classes = [HaohaoJournalServerApplication::class],
    properties = ["spring.main.allow-bean-definition-overriding=true"],
)
@AutoConfigureMockMvc
@Import(TestConfig::class, TestSecurityConfig::class, TestRepositoryConfig::class)
class HaohaoJournalServerApplicationTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun contextLoads() {
        // Verify that the context loads successfully
    }

    @Test
    fun healthCheckEndpoint() {
        mockMvc.perform(get("/health"))
            .andExpect(status().isOk)
            .andExpect(content().string("OK"))
    }
}
