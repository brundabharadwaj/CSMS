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
            throw RuntimeException("Request Timeout", e)
        } catch (e: Exception) {
            throw RuntimeException("Authentication failed", e)
        }
    }




}
