package com.example.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Payload
import com.example.key.Constant
import com.example.key.JwtConstant
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.Date

/**
 * 授权的JWT工具类
 */
object AuthJwtKit {

    fun sign(name: String): Map<String, String> {
        return mapOf(Constant.KEY_TOKEN to makeToken(name), Constant.REFRESH_TOKEN to makeRefreshToken(name))
    }


    fun getPayload(call: ApplicationCall): Payload? {
        val principal = call.authentication.principal as JWTPrincipal?
        return principal?.payload
    }


    private fun makeToken(name: String): String = JWT.create()
        .withSubject(JwtConstant.jwtSubjectAuth)
        .withIssuer(JwtConstant.jwtDomain)
        .withAudience(JwtConstant.jwtAudience)
        .withClaim(Constant.USER_NAME, name)
        .withExpiresAt(getExpiration())
        .sign(Algorithm.HMAC256(JwtConstant.jwtSecret))


    private fun makeRefreshToken(name: String): String = JWT.create()
        .withSubject(JwtConstant.jwtSubjectRefresh)
        .withIssuer(JwtConstant.jwtDomain)
        .withAudience(JwtConstant.jwtAudience)
        .withClaim(Constant.USER_NAME, name)
        .withExpiresAt(getExpirationRefresh())
        .sign(Algorithm.HMAC256(JwtConstant.jwtSecret))


    private fun getExpiration() = Date(System.currentTimeMillis() + JwtConstant.validityInMs)

    private fun getExpirationRefresh() = Date(System.currentTimeMillis() + JwtConstant.validityInMsRefresh)

}