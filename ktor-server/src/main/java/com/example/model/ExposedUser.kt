package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ExposedUser(val userName: String, val password: String)