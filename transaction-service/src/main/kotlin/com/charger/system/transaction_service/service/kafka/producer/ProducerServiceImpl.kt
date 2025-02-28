package com.charger.system.transaction_service.service.kafka.producer;

import KafkaAuthRequest
import SecureAuthorizeRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import com.fasterxml.jackson.databind.ObjectMapper

@Service
class ProducerServiceImpl(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) : ProducerService {

    @Value("\${spring.kafka.producer.topic}")
    private lateinit var topicName: String

    override fun sendMessage(msg: KafkaAuthRequest) {
        // Serialize only SecureAuthorizeRequest as JSON
        val messagePayload = objectMapper.writeValueAsString(msg)

        kafkaTemplate.send(topicName, messagePayload)
        println("✅ Sent message to Kafka: Correlation ID = ${msg.correlationId}, Message = $messagePayload")
    }

}


