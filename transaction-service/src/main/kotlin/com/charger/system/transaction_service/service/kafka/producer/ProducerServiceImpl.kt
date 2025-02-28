package com.charger.system.transaction_service.service.kafka.producer;

import KafkaAuthRequest
import SecureAuthorizeRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import com.fasterxml.jackson.databind.ObjectMapper

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
class ProducerServiceImpl(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) : ProducerService {

    private val logger: Logger = LoggerFactory.getLogger(ProducerServiceImpl::class.java)

    @Value("\${spring.kafka.producer.topic}")
    private lateinit var topicName: String

    override fun sendMessage(msg: KafkaAuthRequest) {
        val messagePayload = objectMapper.writeValueAsString(msg)
        kafkaTemplate.send(topicName, messagePayload)
        logger.info("Sent message to Kafka: Correlation ID = {}", msg.correlationId)
    }
}


