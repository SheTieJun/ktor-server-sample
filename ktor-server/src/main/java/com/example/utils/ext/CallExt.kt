package com.example.utils.ext

import com.auth0.jwt.interfaces.Payload
import com.example.auth.AuthJwtKit
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.origin


fun ApplicationCall.getIp(): String {
    return request.origin.remoteHost
}

fun ApplicationCall.getUA(): String {
    return request.headers["User-Agent"] ?: ""
}

fun ApplicationCall.getReferer(): String {
    return request.headers["Referer"] ?: ""
}

fun ApplicationCall.isMobile(): Boolean {
    //Android /iPhone /iPod /iPad /Windows Phone /MQQBrowser
    return request.headers["User-Agent"]?.contains("Android") == true
            || request.headers["User-Agent"]?.contains("iPhone") == true
            || request.headers["User-Agent"]?.contains("iPod") == true
            || request.headers["User-Agent"]?.contains("iPad") == true
            || request.headers["User-Agent"]?.contains("Windows Phone") == true
            || request.headers["User-Agent"]?.contains("MQQBrowser") == true
}

//获取payload
val ApplicationCall.payload: Payload?
    get() {
        return AuthJwtKit.getPayload(this)
    }