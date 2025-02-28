package com.charging.system.authentication_system.service.kafka.consumer

import KafkaAuthRequest

import com.charging.system.authentication_system.model.AuthenticationStatus
import com.charging.system.authentication_system.service.kafka.producer.KafkaProducer
import com.charging.system.authentication_system.service.validate.ValidationService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

@Service
class KafkaConsumerImpl(
    private val objectMapper: ObjectMapper,
    private val kafkaProducerService: KafkaProducer,
    private val validationService: ValidationService,
    @Value("\${spring.kafka.produce.topic.name}") private val authenticationTopic: String,
) : KafkaConsumer {

    private val logger: Logger = LoggerFactory.getLogger(KafkaConsumerImpl::class.java)

    @KafkaListener(
        topics = ["\${spring.kafka.consume.topic.name}"],
        groupId = "authentication-group",
    )
    override fun listen(msg: String) {
        var correlationId = UUID.randomUUID()
        try {
            logger.info(msg)
            // Parse JSON and extract correlation ID
            val kafkaRequest = objectMapper.readValue(msg, KafkaAuthRequest::class.java)
            correlationId = kafkaRequest.correlationId
            val secureRequest = kafkaRequest.payload
            logger.info("✅ Received authentication request: $secureRequest with Correlation ID: $correlationId")

            // Perform authentication check
            val status = if (validationService.isAuthentic(secureRequest)) {
                logger.info("Authentication successful!")
                AuthenticationStatus.ACCEPTED
            } else {
                logger.warn("Authentication failed!")
                AuthenticationStatus.INVALID
            }
            // Send response back with correlation ID
            val responseMessage = objectMapper.writeValueAsString(
                mapOf("correlationId" to correlationId, "authStatus" to status)
            )
            kafkaProducerService.sendMessage("authentication-status", responseMessage)

            logger.info("Sent authentication response: $status for Correlation ID: $correlationId")

        } catch (e: Exception) {
            logger.error("Error processing message: ${e.message}", e)

            // Send failure response with extracted or fallback correlation ID
            val errorResponse = objectMapper.writeValueAsString(
                mapOf("correlationId" to correlationId, "authStatus" to AuthenticationStatus.INVALID)
            )
            kafkaProducerService.sendMessage("authentication-status", errorResponse)
        }
    }
}
