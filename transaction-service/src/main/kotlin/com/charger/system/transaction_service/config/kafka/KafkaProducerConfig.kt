package com.charger.system.transaction_service.config.kafka

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaProducerConfig {

    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        val configProps = mutableMapOf<String, Any>(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:29092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.ACKS_CONFIG to "all",
            ProducerConfig.RETRIES_CONFIG to 3,
            ProducerConfig.BATCH_SIZE_CONFIG to 16_384,
            ProducerConfig.LINGER_MS_CONFIG to 1
        )
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, String>): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory)
    }
}
