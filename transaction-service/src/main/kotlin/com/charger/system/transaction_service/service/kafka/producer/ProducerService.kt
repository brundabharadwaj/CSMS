package com.charger.system.transaction_service.service.kafka.producer

import KafkaAuthRequest


interface ProducerService {
    fun sendMessage(msg:KafkaAuthRequest)
}