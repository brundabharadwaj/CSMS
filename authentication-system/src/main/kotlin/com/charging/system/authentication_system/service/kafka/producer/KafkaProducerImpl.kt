package com.charging.system.authentication_system.service.kafka.producer

import com.charging.system.authentication_system.service.kafka.consumer.KafkaConsumerImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducerImpl(
    private val kafkaTemplate: KafkaTemplate<String, String>):KafkaProducer {
    private val logger: Logger = LoggerFactory.getLogger(KafkaConsumerImpl::class.java)
    override fun sendMessage(topic: String, message: String) {
        try {
            logger.info("sending back the auth details.....")
            kafkaTemplate.send(topic, message)
            logger.info("Message published to topic '$topic': $message")
        } catch (e: Exception) {
            logger.info("Error  while publishing message : ${e.message}")
        }
    }
}
