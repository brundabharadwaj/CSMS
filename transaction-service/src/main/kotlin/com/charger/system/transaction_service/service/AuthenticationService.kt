package com.charger.system.transaction_service.service

import AuthorizeRequest
import com.charger.system.transaction_service.model.AuthenticationStatus
import com.charger.system.transaction_service.model.AuthorizationResponse

interface AuthenticationService {
    fun authenticate(details: AuthorizeRequest):AuthenticationStatus

}
