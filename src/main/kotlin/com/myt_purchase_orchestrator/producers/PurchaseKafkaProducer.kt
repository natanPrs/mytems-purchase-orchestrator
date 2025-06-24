package com.myt_purchase_orchestrator.producers

import com.myt_purchase_orchestrator.dtos.EventSavePurchaseDto
import com.myt_purchase_orchestrator.dtos.EventUpdateItemStatusDto
import com.myt_purchase_orchestrator.dtos.EventUpdateUserDto
import com.myt_purchase_orchestrator.dtos.PurchaseDto
import com.myt_purchase_orchestrator.mappers.toEventUpdateStatus
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import kotlin.math.log

@Component
class PurchaseKafkaProducer(
    private val eventSavePurchaseTemplate: KafkaTemplate<String, EventSavePurchaseDto>,
    private val eventUpdateUserTemplate: KafkaTemplate<String, EventUpdateUserDto>,
    private val eventUpdateItemStatusDtoTemplate: KafkaTemplate<String, EventUpdateItemStatusDto>
) {

    private val logger = LoggerFactory.getLogger(PurchaseKafkaProducer::class.java)

    fun sendSavePurchaseEvent(purchaseDto: PurchaseDto, sellerId: UUID, amount: BigDecimal){
        val event = EventSavePurchaseDto(
            globalItemId = purchaseDto.globalItemId,
            sellerId = sellerId,
            buyerId = purchaseDto.buyerId,
            amount = amount,
            localStamp = LocalDateTime.now())

        eventSavePurchaseTemplate.send("savePurchase", event)
        logger.debug("Kafka event sent.")
    }

    fun sendItemStatusEvent(purchaseDto: PurchaseDto){
        val event = purchaseDto.toEventUpdateStatus()
        eventUpdateItemStatusDtoTemplate.send("updateItemStatus", event)
        logger.debug("Kafka event sent")
    }

    fun sendUserUpdateEvent(amount: BigDecimal, buyerId : UUID, itemId: UUID){
        val event = EventUpdateUserDto(amount, buyerId ,itemId)
        eventUpdateUserTemplate.send("updateUser", event)
    }
}