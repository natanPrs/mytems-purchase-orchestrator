package com.myt_purchase_orchestrator.outbox.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.myt_purchase_orchestrator.dtos.EventSavePurchaseDto
import com.myt_purchase_orchestrator.producers.PurchaseKafkaProducer
import org.springframework.stereotype.Component

@Component
class SendSavePurchaseEvent(
    private val purchaseKafkaProducer: PurchaseKafkaProducer,
    private val objectMapper: ObjectMapper,
) : EventStrategy {
    override fun sendToKafkaTopic(payload: String) {
        val eventDeserialized = objectMapper.readValue(payload, EventSavePurchaseDto::class.java)
        purchaseKafkaProducer.sendSavePurchaseEvent(eventDeserialized)
    }
}