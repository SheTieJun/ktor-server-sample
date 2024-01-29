// 拓展 ApplicationCall json返回
package com.example.utils.ext

import com.example.model.RespondResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

// 统一异常返回

// 200 成功 | 失败

// 所有异常返回模板
suspend fun ApplicationCall.jsonError(code: HttpStatusCode, msg: String) {
    respond(code, RespondResult.error(code.value, msg))
}

// 200 - success
suspend fun ApplicationCall.jsonOk(data: Any? = null) {
    respond(HttpStatusCode.OK,RespondResult.success(data = data))
}

// 200 - error
suspend fun ApplicationCall.jsonError(errCode: Int = HttpStatusCode.BadRequest.value, msg: String) {
    respond(HttpStatusCode.OK,RespondResult.error(errCode, msg))
}


suspend fun ApplicationCall.jsonError(msg: String) {
    respond(HttpStatusCode.OK,RespondResult.error(HttpStatusCode.BadRequest.value, msg))
}

// 500
suspend fun ApplicationCall.json500(errMsg: String? = null) {
    jsonError(HttpStatusCode.InternalServerError, errMsg ?: "服务器内部错误")
}

// 404
suspend fun ApplicationCall.json404(errMsg: String? = null) {
    jsonError(HttpStatusCode.NotFound, errMsg?:"找不到的路径")
}

// 405
suspend fun ApplicationCall.json405() {
    jsonError(HttpStatusCode.NotFound, "资源被禁止")
}

// 403
suspend fun ApplicationCall.json403(msg: String? = null) {
    jsonError(HttpStatusCode.Forbidden, msg ?: "验证失败禁止访问")
}

// 401
suspend fun ApplicationCall.json401(msg: String? = null) {
    jsonError(HttpStatusCode.Unauthorized, msg ?: "当前请求需要验证")
}
