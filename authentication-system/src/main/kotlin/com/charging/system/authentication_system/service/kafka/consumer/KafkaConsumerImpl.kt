package com.charging.system.authentication_system.service.kafka.consumer


import com.charging.system.authentication_system.service.kafka.KafkaMessageProcessor
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

@Service
class KafkaConsumerImpl(
    private val kafkaMessageProcessor: KafkaMessageProcessor,
    @Value("\${spring.kafka.produce.topic.name}") private val authenticationTopic: String,
) : KafkaConsumer {

    private val logger: Logger = LoggerFactory.getLogger(KafkaConsumerImpl::class.java)

    @KafkaListener(
        topics = ["\${spring.kafka.consume.topic.name}"],
        groupId = "authentication-group",
    )
    override fun listen(msg: String) {
        kafkaMessageProcessor.processAndSendMessage(msg, authenticationTopic)

    }
}

