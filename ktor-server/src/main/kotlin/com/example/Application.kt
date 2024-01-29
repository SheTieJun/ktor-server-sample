package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configKoin() //依赖倒置：数据库等
    configStaticResources() //HTML静态资源
    configureRateLimit() //速率限制
    configureSecurity() //安全处理,JWT
    configureAdministration() //管理员
    configureSockets() //socket处理
    configureTemplating()//模板处理
    configureSerialization() //序列化处理
    configureProcessException() //异常处理
    configureLogging() //日志处理
    configureHTTP() //HTTP处理
    configureRouting() //路由处理,一般放在最后
}
