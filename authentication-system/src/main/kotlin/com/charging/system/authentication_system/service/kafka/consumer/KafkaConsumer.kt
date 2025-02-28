package com.charging.system.authentication_system.service.kafka.consumer

interface KafkaConsumer {
    fun listen(msg:String)
}