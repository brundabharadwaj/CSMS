package com.charger.system.transaction_service.model

data class AuthorizationResponse(
    val status: String,
    val token: String? = null // JWT token or access token
)