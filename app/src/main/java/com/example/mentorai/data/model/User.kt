package com.example.mentorai.data.model

import java.util.Date

data class User (
    val uid: String = "",
    val name: String? = null,
    val email: String,
    val password: String,
    val profilePhotoUrl: String? = null,
    val createdAt: Date = Date(),
    val lastLogin: Date = Date(),
    val preferences: Map<String, Any>? = null
)