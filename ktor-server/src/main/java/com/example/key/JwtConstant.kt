package com.example.key

object JwtConstant {

    const val jwtAudience = "jwt-audience-belove"
    const val jwtDomain = "https://jwt-provider-domain/"
    const val jwtRealm = "ktor sample app"
    const val jwtSecret = "jwt-belove-secret"
    const val jwtSubjectAuth = "Authentication"
    const val jwtSubjectRefresh = "Refresh"
    const val validityInMs = 3600 * 1000 * 10 // 10 hours
    const val validityInMsRefresh =  3600 * 1000 * 24 * 7 // 7 days
}