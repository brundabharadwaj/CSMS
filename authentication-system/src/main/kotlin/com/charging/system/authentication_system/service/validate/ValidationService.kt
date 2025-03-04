package com.charging.system.authentication_system.service.validate

import SecureAuthorizeRequest

interface ValidationService {

    fun isRequestAuthentic(request: SecureAuthorizeRequest): Boolean
}