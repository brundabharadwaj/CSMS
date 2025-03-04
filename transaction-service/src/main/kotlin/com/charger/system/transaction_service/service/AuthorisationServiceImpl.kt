package com.charger.system.transaction_service.service

import AuthorizeRequest
import com.charger.system.transaction_service.model.AuthenticationStatus
import com.charger.system.transaction_service.model.AuthorizationResponse
import org.springframework.stereotype.Service

@Service
class AuthorisationServiceImpl ():AuthorizationService{
    override fun authorisation(details: AuthorizeRequest, authenticationResponse: AuthenticationStatus): AuthorizationResponse {
        return if (authenticationResponse == AuthenticationStatus.ACCEPTED) {
            AuthorizationResponse(AuthenticationStatus.ACCEPTED.toString(),generateJwtToken())
        } else {
            AuthorizationResponse(AuthenticationStatus.INVALID.toString(),null)
        }
    }

    private fun generateJwtToken(): String {
        // Dummy Jwt just for demonstration purpose...
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
    }
}