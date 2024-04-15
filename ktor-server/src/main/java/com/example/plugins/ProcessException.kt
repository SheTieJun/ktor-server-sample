package com.example.plugins

import com.example.model.RespondResult
import com.example.utils.ext.json404
import com.example.utils.ext.jsonError
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import io.netty.handler.timeout.TimeoutException
import javax.naming.AuthenticationException

/**
 * 处理为捕捉的异常
 */
fun Application.configureProcessException() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is AuthenticationException -> call.respond(HttpStatusCode.OK, RespondResult.error(HttpStatusCode.Unauthorized.value, cause.message ?: HttpStatusCode.Unauthorized.description))
                is NotFoundException -> call.respond(HttpStatusCode.OK, RespondResult.error(HttpStatusCode.NotFound.value, cause.message ?: HttpStatusCode.NotFound.description))
                is IllegalArgumentException, is NullPointerException, is IllegalStateException, is BadRequestException ->{
                    call.respond(HttpStatusCode.OK, RespondResult.error(HttpStatusCode.BadRequest.value, cause.message ?: HttpStatusCode.BadRequest.description))
                }
                is TimeoutException -> call.respond(HttpStatusCode.OK, RespondResult.error(HttpStatusCode.RequestTimeout.value, cause.message ?: HttpStatusCode.RequestTimeout.description))
                is ArithmeticException -> call.respond(HttpStatusCode.OK, RespondResult.error(HttpStatusCode.Conflict.value, cause.message ?: HttpStatusCode.Conflict.description))
                else -> call.respond(HttpStatusCode.OK, RespondResult.error(HttpStatusCode.InternalServerError.value, cause.message ?: HttpStatusCode.InternalServerError.description))
            }
        }
        status(HttpStatusCode.TooManyRequests) { call, _ ->
            val retryAfter = call.response.headers["Retry-After"]
            call.jsonError(HttpStatusCode.TooManyRequests.value,"Too many requests. Wait for $retryAfter seconds.")
        }
        status(HttpStatusCode.MethodNotAllowed){ call, _ ->
            call.jsonError(HttpStatusCode.MethodNotAllowed.value,"method ${call.request.uri} NOT ALLOWED")
        }
        status(HttpStatusCode.NotFound) { call, _ ->
            call.json404("method ${call.request.uri} not found")
        }
        HttpStatusCode.allStatusCodes.forEach {
            if (it == HttpStatusCode.OK
                || it == HttpStatusCode.TooManyRequests
                || it == HttpStatusCode.MethodNotAllowed
                || it == HttpStatusCode.NotFound) {
                return@forEach
            }
            if (it.value in 400..599) { //只处理错误的状态码
                status(it) { call, _ ->
                    call.jsonError(it.value,"${it.value}: ${it.description}")
                }
                return@forEach
            }
        }
    }
}