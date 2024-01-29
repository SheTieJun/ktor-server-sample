package com.example.handle

import com.example.db.UserService
import com.example.db.UserService.Users.id
import com.example.key.Constant
import com.example.key.RedisConstant
import com.example.model.ExposedUser
import com.example.model.RespondResult
import com.example.utils.ext.jsonError
import com.example.utils.ext.jsonOk
import com.example.utils.ext.toBean
import com.example.utils.ext.toJson
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.koin.ktor.ext.inject
import redis.clients.jedis.JedisPool
import redis.clients.jedis.params.SetParams

/**
 * 处理用户信息
 */
fun Route.handleUser() {
    val userService by inject<UserService>()
    val redisPool by inject<JedisPool>()
    authenticate {
        get("/userinfo") {
            val redis = redisPool.resource
            redisPool.resource.apply {
                auth(RedisConstant.REDIS_PASSWORD)
            }.use {
                val user = call.request.queryParameters[Constant.USER_NAME]?:return@get call.jsonError("userName is null")
                if (it.exists(user)) {
                    call.jsonOk(redis.get(user).toBean<ExposedUser>())
                    return@get
                }
                val userInfo = userService.read(user)
                redis.set(user, userInfo.toJson(), SetParams.setParams().apply {
                    this.pxAt(System.currentTimeMillis() + 1000 * 60 * 60 * 24)
                })
                call.jsonOk(userInfo)
            }
        }
    }
}