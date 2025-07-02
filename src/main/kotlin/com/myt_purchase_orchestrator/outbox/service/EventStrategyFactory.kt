package com.myt_purchase_orchestrator.outbox.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class EventStrategyFactory(
    private val sendItemStatusEvent: SendItemStatusEvent,
    private val sendSavePurchaseEvent: SendSavePurchaseEvent,
    private val sendUserUpdateEvent: SendUserUpdateEvent,
) {
    val logger = LoggerFactory.getLogger(EventStrategyFactory::class.java)

    fun createEvent(event: String): EventStrategy {
        return when (event) {
            "SEND_SAVE_PURCHASE_EVENT" -> sendSavePurchaseEvent
            "SEND_ITEM_STATUS_EVENT" -> sendItemStatusEvent
            "SEND_USER_UPDATE_EVENT" -> sendUserUpdateEvent
            else -> {
                logger.warn("Unknow event type: {}", event)
                throw Exception("Invalid eventType")
            }
        }
    }
}