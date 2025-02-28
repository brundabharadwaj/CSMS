package com.charger.system.transaction_service.service.kafka.consumer

import com.charger.system.transaction_service.model.AuthenticationStatus
import com.charger.system.transaction_service.model.ResponseStore

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
class KafkaConsumerImpl(
    private val objectMapper: ObjectMapper,
    private val responseStore: ResponseStore  // Stores request-response mappings
) : KafkaConsumer {

    private val logger: Logger = LoggerFactory.getLogger(KafkaConsumerImpl::class.java)

    @KafkaListener(
        topics = ["authentication-status"],
        groupId = "transaction-group"
    )
    override fun listen(msg: String) {
        try {
            logger.info("📩 Received message: $msg")
            val jsonNode = objectMapper.readTree(msg)

            val correlationId = jsonNode["correlationId"]?.asText() ?: run {
                logger.warn("⚠️ Missing correlationId in message")
                return
            }

            val authStatus = jsonNode["authStatus"]?.asText()?.let {
                runCatching { AuthenticationStatus.valueOf(it) }.getOrElse {
                    logger.warn("⚠️ Invalid authStatus received: $it for correlationId: $correlationId. Defaulting to UNKNOWN.")
                    AuthenticationStatus.UNKNOWN
                }
            } ?: run {
                logger.warn("⚠️ Missing authStatus in message for correlationId: $correlationId")
                return
            }

            // Handle 'EXCEPTION_AROSE' case specifically
            val resolvedAuthStatus = if (authStatus == AuthenticationStatus.UNKNOWN) {
                logger.warn("⚠️ Exception occurred for correlationId: $correlationId. Marking as 'Unknown'")
                AuthenticationStatus.UNKNOWN
            } else {
                authStatus
            }

            logger.info("✅ Processed authentication response: $resolvedAuthStatus for Correlation ID: $correlationId")

            // Complete the waiting future with the processed authentication status
            responseStore.completeFuture(correlationId, resolvedAuthStatus)

        } catch (e: Exception) {
            logger.error("❌ Error processing Kafka message: ${e.localizedMessage}", e)
        }
    }
}

