package com.charging.system.authentication_system.service.validate

import SecureAuthorizeRequest

interface ValidationService {
   // fun validateAuthentication(request: SecureAuthorizeRequest): Boolean
    fun isAuthentic(request: SecureAuthorizeRequest): Boolean
}