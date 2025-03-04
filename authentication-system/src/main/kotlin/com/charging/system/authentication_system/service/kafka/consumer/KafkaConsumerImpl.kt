package com.charging.system.authentication_system.service.kafka.consumer


import com.charging.system.authentication_system.service.kafka.KafkaMessageProcessor
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumerImpl(
    private val kafkaMessageProcessor: KafkaMessageProcessor,
    @Value("\${spring.kafka.produce.topic.name}") private val authenticationTopic: String,
) : KafkaConsumer {



    @KafkaListener(
        topics = ["\${spring.kafka.consume.topic.name}"],
        groupId = "authentication-group",
    )
    override fun listen(msg: String) {
        kafkaMessageProcessor.processAndSendMessage(msg, authenticationTopic)

    }
}

