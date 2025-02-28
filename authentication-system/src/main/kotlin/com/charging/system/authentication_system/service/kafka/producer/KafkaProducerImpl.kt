package com.charging.system.authentication_system.service.kafka.producer

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class KafkaProducerImpl(
    private val kafkaTemplate: KafkaTemplate<String, String>):KafkaProducer {

    override fun sendMessage(topic: String, message: String) {
        try {
            println("sending message back...")
            kafkaTemplate.send(topic, message)
            println("Message sent to topic '$topic': $message")
        } catch (e: Exception) {
            println("Error sending message to Kafka: ${e.message}")
        }
    }
}
