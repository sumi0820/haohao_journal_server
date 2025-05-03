package com.haohao.journal.server.base

import com.fasterxml.jackson.databind.ObjectMapper
import com.haohao.journal.server.config.TestSecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

@AutoConfigureMockMvc
@Import(TestSecurityConfig::class)
abstract class BaseControllerTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    protected fun MockHttpServletRequestBuilder.jsonContent(content: Any): MockHttpServletRequestBuilder =
        this.contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(content))
}
