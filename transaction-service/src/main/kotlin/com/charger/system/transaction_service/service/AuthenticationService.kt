package com.charger.system.transaction_service.service

import AuthorizeRequest
import com.charger.system.transaction_service.model.AuthenticationStatus

interface AuthenticationService {
    fun authenticate(details: AuthorizeRequest):AuthenticationStatus
     fun authorisation(details: AuthorizeRequest, authenticationResponse: AuthenticationStatus): String
}
