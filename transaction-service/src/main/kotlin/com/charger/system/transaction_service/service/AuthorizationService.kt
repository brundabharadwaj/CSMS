package com.charger.system.transaction_service.service

import AuthorizeRequest
import com.charger.system.transaction_service.model.AuthenticationStatus
import com.charger.system.transaction_service.model.AuthorizationResponse

interface AuthorizationService {
    fun authorisation(details: AuthorizeRequest, authenticationResponse: AuthenticationStatus): AuthorizationResponse
}