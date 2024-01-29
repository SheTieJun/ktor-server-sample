package com.example.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import kotlin.time.Duration.Companion.seconds

/**
 * 配置速率限制
 */
fun Application.configureRateLimit() {
    install(RateLimit) {
        global {
            rateLimiter(limit = 50, refillPeriod = 60.seconds)
        }
        register(RateLimitName("login")) {
            rateLimiter(limit = 10, refillPeriod = 60.seconds)
        }
    }
}