package com.myt_purchase_orchestrator.producers

import com.myt_purchase_orchestrator.dtos.EventSavePurchaseDto
import com.myt_purchase_orchestrator.dtos.EventUpdateItemStatusDto
import com.myt_purchase_orchestrator.dtos.EventUpdateUserDto
import com.myt_purchase_orchestrator.dtos.PurchaseDto
import com.myt_purchase_orchestrator.mappers.toEventUpdateStatus
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class PurchaseKafkaProducer(
    private val eventSavePurchaseTemplate: KafkaTemplate<String, EventSavePurchaseDto>,
    private val eventUpdateUserTemplate: KafkaTemplate<String, EventUpdateUserDto>,
    private val eventUpdateItemStatusDtoTemplate: KafkaTemplate<String, EventUpdateItemStatusDto>
) {

    private val logger = LoggerFactory.getLogger(PurchaseKafkaProducer::class.java)

    fun sendSavePurchaseEvent(event: EventSavePurchaseDto){
        eventSavePurchaseTemplate.send("savePurchase", event)
        logger.debug("Kafka event sent.")
    }

    fun sendItemStatusEvent(event: PurchaseDto){
        val event = event.toEventUpdateStatus()
        eventUpdateItemStatusDtoTemplate.send("updateItemStatus", event)
        logger.debug("Kafka event sent")
    }

    fun sendUserUpdateEvent(event: EventUpdateUserDto){
        eventUpdateUserTemplate.send("updateUser", event)
    }
}