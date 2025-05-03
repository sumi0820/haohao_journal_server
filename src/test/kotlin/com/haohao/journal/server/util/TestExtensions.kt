package com.haohao.journal.server.util

import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

fun ResultActions.andExpectSuccess(): ResultActions =
    this.andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk)

fun ResultActions.andExpectCreated(): ResultActions =
    this.andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isCreated)

fun ResultActions.andExpectBadRequest(): ResultActions =
    this.andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest)

fun ResultActions.andExpectNotFound(): ResultActions =
    this.andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound)

fun ResultActions.andExpectUnauthorized(): ResultActions =
    this.andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isUnauthorized)

fun ResultActions.andExpectForbidden(): ResultActions =
    this.andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isForbidden)
