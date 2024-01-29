package com.example.handle

import com.example.auth.AuthJwtKit
import com.example.db.UserService
import com.example.key.Constant
import com.example.model.ExposedUser
import com.example.model.LoginModel
import com.example.model.RespondResult
import com.example.utils.ext.json403
import com.example.utils.ext.jsonError
import com.example.utils.ext.payload
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

/**
 * Handle login
 * 登录/注册逻辑处理
 */
fun Route.handleLoginAndRegister() {
    val userService by inject<UserService>()
    route("/user") {
        rateLimit(RateLimitName("login")) { //登录增加限制，1分组内最多10次
            registerCase(userService)
            loginCase(userService)
            refreshToken()
        }
    }
}

private fun Route.refreshToken() {
    authenticate("refreshToken") {
        get("/refresh") {
            val principal = call.payload
            if (principal != null) {
                val userName = (principal.claims[Constant.USER_NAME]?.asString()) ?: throw IllegalArgumentException("登录失效请重新登录")
                call.respond(RespondResult.success(AuthJwtKit.sign(userName)))
            } else {
                call.json403("登录失效请重新登录")
            }
        }
    }
}

private fun Route.registerCase(userService: UserService) {
    route("/register") {
        post {
            try {
                val user = call.receive<ExposedUser>()
                checkNotNull(user.userName) { "${Constant.USER_NAME}不能为空" }
                checkNotNull(user.password) { "${Constant.PASSWORD}不能为空" }
                if (userService.read(user.userName) != null) {
                    call.jsonError(msg = "用户名称已存在")
                    return@post
                }
                userService.create(user)
                call.respond(RespondResult.success())
            } catch (e: Exception) {
                throw e
            }
        }
    }
}

private fun Route.loginCase(userService: UserService) {
    route("/login") {
        //登录接口
        post {
            try {
                val user = call.receive<LoginModel>()
                checkNotNull(user.userName) { "${Constant.USER_NAME}不能为空" }
                checkNotNull(user.password) { "${Constant.PASSWORD}不能为空" }
                val exposedUser = userService.read(user.userName)
                if (exposedUser == null) {
                    call.jsonError("用户不存在")
                    return@post
                }
                if (exposedUser.password != user.password) {
                    call.jsonError("密码错误")
                    return@post
                }
                //这里可以去数据库查询用户信息,是否存在和正确
                call.respond(RespondResult.success(AuthJwtKit.sign(exposedUser.userName)))
            } catch (e: Exception) {
                throw e
            }
        }
        get {
            try {
                val user = call.request.queryParameters
                val userName = user[Constant.USER_NAME] ?: throw IllegalArgumentException("${Constant.USER_NAME}不能为空")
                val password = user[Constant.PASSWORD] ?: throw IllegalArgumentException("${Constant.PASSWORD}不能为空")
                val exposedUser = userService.read(userName)
                if (exposedUser == null) {
                    call.jsonError("用户不存在")
                    return@get
                }
                if (exposedUser.password != password) {
                    call.jsonError("密码错误")
                    return@get
                }
                //这里可以去数据库查询用户信息,是否存在和正确
                call.respond(RespondResult.success(AuthJwtKit.sign(userName)))
            } catch (e: Exception) {
                throw e
            }
        }
    }
}