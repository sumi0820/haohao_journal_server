package com.haohao.journal.server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {
    companion object {
        private const val HEALTH_STATUS = "OK"
    }

    @GetMapping("/health")
    fun healthCheck(): String = HEALTH_STATUS
}
