package com.myt_purchase_orchestrator.configs

import com.myt_purchase_orchestrator.dtos.EventSavePurchaseDto
import com.myt_purchase_orchestrator.dtos.EventUpdateItemStatusDto
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaProducerConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    val bootstrapServer: String) {

    private fun <T> buildProducerFactory(): ProducerFactory<String, T>{
        val config = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServer,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
        )
        return DefaultKafkaProducerFactory(config)
    }

    fun eventSavePurchaseDtoTemplate(): KafkaTemplate<String, EventSavePurchaseDto> =
        KafkaTemplate(buildProducerFactory())

    fun eventUpdateItemStatusTemplate(): KafkaTemplate<String, EventUpdateItemStatusDto> =
        KafkaTemplate(buildProducerFactory())
}