package com.example.plugins

import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing

fun Application.configStaticResources() {
    routing {
        staticResources("/static","static/assets")
    }
}