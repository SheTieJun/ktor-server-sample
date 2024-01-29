package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.configureHTTP() {
    install(CORS){
        anyHost()//允许任何host

    }
    routing {
        //swagger 接口文档
        swaggerUI(path = "openapi")
    }
}
