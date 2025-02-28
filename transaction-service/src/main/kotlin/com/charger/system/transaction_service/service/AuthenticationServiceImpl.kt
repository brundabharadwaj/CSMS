package com.charger.system.transaction_service.service
import AuthorizeRequest
import KafkaAuthRequest
import com.charger.system.transaction_service.model.AuthenticationStatus
import com.charger.system.transaction_service.model.AuthorizationResponse
import com.charger.system.transaction_service.model.ResponseStore
import com.charger.system.transaction_service.service.kafka.producer.ProducerService
import org.apache.kafka.common.errors.TimeoutException
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class AuthenticationServiceImpl(
    private val producerService: ProducerService,
    private val responseStore: ResponseStore  // Store pending responses
) : AuthenticationService {

    override fun authenticate(details: AuthorizeRequest): AuthenticationStatus {
        return try {
            val correlationId = UUID.randomUUID().toString()
            val future = responseStore.createFuture(correlationId)
            val secureRequest = SecureAuthorizeRequest.fromAuthorizeRequest(details)
            val kafkaAuthRequest = KafkaAuthRequest(correlationId, secureRequest)
            producerService.sendMessage(kafkaAuthRequest)
            future.get(5, TimeUnit.SECONDS)
        } catch (e: TimeoutException) {
            throw RuntimeException("Authentication timed out", e)
        } catch (e: Exception) {
            throw RuntimeException("Authentication failed", e)
        }
    }

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
