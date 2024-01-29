package com.example.plugins

import io.ktor.server.application.Application

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.Level
/**
 *
 * <b>@author：</b> shetj<br>
 * <b>@createTime：</b> 2024/1/23<br>
 * <b>@email：</b> 375105540@qq.com<br>
 * <b>@describe</b>  <br>
 */
fun Application.configureLogging() {
    install(CallLogging) {
        level = Level.INFO
        format { call ->
            val path = call.request.path()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]

            "Path: $path, HTTPMethod: $httpMethod, UserAgent: $userAgent"
        }
    }
}
