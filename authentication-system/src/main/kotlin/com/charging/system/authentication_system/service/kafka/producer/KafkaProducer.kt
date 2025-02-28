package com.charging.system.authentication_system.service.kafka.producer

interface KafkaProducer {
    fun sendMessage(topic: String, message: String)
}