package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.key.JwtConstant
import com.example.utils.ext.json403
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    install(Authentication) {
        jwt {
            realm = JwtConstant.jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(JwtConstant.jwtSecret))
                    .withSubject(JwtConstant.jwtSubjectAuth)
                    .withAudience(JwtConstant.jwtAudience)
                    .withIssuer(JwtConstant.jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(JwtConstant.jwtAudience)
                    && credential.payload.subject == JwtConstant.jwtSubjectAuth) {
                    JWTPrincipal(credential.payload)
                } else {
                    this.json403("JWT验证失败")
                    null
                }
            }
        }

        jwt("refreshToken") {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(JwtConstant.jwtSecret))
                    .withSubject(JwtConstant.jwtSubjectRefresh)
                    .withAudience(JwtConstant.jwtAudience)
                    .withIssuer(JwtConstant.jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(JwtConstant.jwtAudience)
                    && credential.payload.subject == JwtConstant.jwtSubjectRefresh) {
                    JWTPrincipal(credential.payload)
                } else {
                    this.json403("Invalid token")
                    null
                }
            }
        }
    }
}
