package com.haohao.journal.server.util

import java.time.LocalDateTime

object TestDataFactory {
    fun createCurrentDateTime(): LocalDateTime = LocalDateTime.now()

    fun createRandomString(length: Int = 10): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun createRandomEmail(): String {
        val username = createRandomString(8)
        val domain = createRandomString(6)
        return "$username@$domain.com"
    }
}
