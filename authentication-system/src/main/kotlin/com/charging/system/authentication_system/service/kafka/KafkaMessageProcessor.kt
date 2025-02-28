package com.charging.system.authentication_system.service.kafka

import KafkaAuthRequest
import com.charging.system.authentication_system.model.AuthenticationStatus
import com.charging.system.authentication_system.service.kafka.producer.KafkaProducer
import com.charging.system.authentication_system.service.validate.ValidationService
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*


@Service
class KafkaMessageProcessor(
    private val objectMapper: ObjectMapper,
    private val validationService: ValidationService,
    private val kafkaProducerService: KafkaProducer
) {

    private val logger: Logger = LoggerFactory.getLogger(KafkaMessageProcessor::class.java)

    fun processAndSendMessage(msg: String, topic: String) {
        var correlationId = UUID.randomUUID()
        try {
            // Parse the message
            val kafkaRequest = objectMapper.readValue(msg, KafkaAuthRequest::class.java)
            correlationId = kafkaRequest.correlationId
            val secureRequest = kafkaRequest.payload
            logger.info("Received authentication request: $secureRequest with Correlation ID: $correlationId")

            // Perform authentication check
            val status = if (validationService.isAuthentic(secureRequest)) {
                logger.info("Authentication successful!")
                AuthenticationStatus.ACCEPTED
            } else {
                logger.warn("Authentication failed!")
                AuthenticationStatus.INVALID
            }

            // Prepare the response message
            val responseMessage = objectMapper.writeValueAsString(
                mapOf("correlationId" to correlationId, "authStatus" to status)
            )

            // Send the response message via Kafka
            kafkaProducerService.sendMessage(topic, responseMessage)
            logger.info("Sent authentication response: $responseMessage for Correlation ID: $correlationId")
        } catch (e: Exception) {
            logger.error("Error processing message: ${e.message}", e)

            // Prepare and send failure response
            val errorResponse = objectMapper.writeValueAsString(
                mapOf("correlationId" to correlationId, "authStatus" to AuthenticationStatus.INVALID)
            )
            kafkaProducerService.sendMessage(topic, errorResponse)
        }
    }
}
