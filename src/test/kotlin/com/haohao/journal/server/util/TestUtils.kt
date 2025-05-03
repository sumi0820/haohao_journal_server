package com.haohao.journal.server.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

val objectMapper = ObjectMapper()

fun <T> ResultActions.andExpectJson(expected: T): ResultActions {
    return this
        .andExpect(status().isOk)
        .andExpect(content().contentType("application/json"))
        .andExpect(content().json(objectMapper.writeValueAsString(expected)))
}

inline fun <reified T> ResultActions.andReturnJson(): T {
    val response = this.andReturn().response.contentAsString
    return objectMapper.readValue(response)
}
