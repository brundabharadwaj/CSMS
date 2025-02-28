package com.charger.system.transaction_service.service.kafka.consumer

interface KafkaConsumer {
    fun listen(msg:String)
}