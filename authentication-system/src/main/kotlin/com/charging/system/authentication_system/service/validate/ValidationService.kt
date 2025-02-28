package com.charging.system.authentication_system.service.validate

import SecureAuthorizeRequest

interface ValidationService {

    fun isAuthentic(request: SecureAuthorizeRequest): Boolean
}