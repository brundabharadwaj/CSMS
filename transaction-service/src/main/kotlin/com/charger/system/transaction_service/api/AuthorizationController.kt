package com.charger.system.transaction_service.api

import AuthorizeRequest
import com.charger.system.transaction_service.service.AuthenticationServiceImpl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@RestController
@RequestMapping("/api/authorize")
@Tag(name = "Authorization", description = "APIs for handling authorization requests")
class AuthorizationController(
    private val authorizationService: AuthenticationServiceImpl
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)


    @PostMapping
    @Operation(
        summary = "Authorize a request",
        description = "Processes an authorization request and returns the authentication status."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Authorization processed successfully"),
            ApiResponse(responseCode = "400", description = "Invalid request body"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun handleRequest(@Valid @RequestBody details: AuthorizeRequest): ResponseEntity<String> {
            logger.info("🔹 Received authorization request: {}", details)
            val authenticationResponse = authorizationService.authenticate(details)
            val authorizationStatus = authorizationService.authorisation(details, authenticationResponse)
            logger.info("Authorization status: {}", authorizationStatus)
            return  ResponseEntity.ok(authorizationStatus)
    }
}
