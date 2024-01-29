package com.example.plugins

import com.example.handle.handleLoginAndRegister
import com.example.handle.handleUser
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        get("/") {
            //重定向
            call.respondRedirect("/index")
        }
        handleLoginAndRegister() //登录
        handleUser() //用户系统
    }
}


