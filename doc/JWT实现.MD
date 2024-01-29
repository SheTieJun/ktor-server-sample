## JWT实现  https://ktor.io/docs/jwt.html
```kotlin
implementation("io.ktor:ktor-server-auth-jvm")
implementation("io.ktor:ktor-server-auth-jwt-jvm")
```

## 构建token

```kotlin
   private fun makeToken(name: String): String = JWT.create()
        .withSubject(JwtConstant.jwtSubjectAuth)
        .withIssuer(JwtConstant.jwtDomain)
        .withAudience(JwtConstant.jwtAudience)
        .withClaim(Constant.USER_NAME, name)
        .withExpiresAt(getExpiration())
        .sign(Algorithm.HMAC256(JwtConstant.jwtSecret))
```

<aside>
💡 这里我们需要区分业务请求的Token和RefreshToken，在获取的时候你可以一些区分,然后再不同路由处理添加对应限制,比如普通token和refreshToken
</aside>


### 配置验证

```kotlin
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
```

### 应用：验证普通token和验证refreshToken

```kotlin
//普通请求验证token
authenticate {
        get("/userinfo") {
            call.jsonOk()
        }
}
//refreshToken 验证
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
```