package com.myt_purchase_orchestrator.outbox.schedule

import com.myt_purchase_orchestrator.enums.OutboxEventStatus
import com.myt_purchase_orchestrator.outbox.repository.OutboxRepository
import com.myt_purchase_orchestrator.outbox.service.sendItemStatusEvent
import com.myt_purchase_orchestrator.outbox.service.sendSavePurchaseEvent
import com.myt_purchase_orchestrator.outbox.service.sendUserUpdateEvent
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OutboxSchedule(
    private val outboxRepository: OutboxRepository,
    private val sendItemStatusEvent: sendItemStatusEvent,
    private val sendSavePurchaseEvent: sendSavePurchaseEvent,
    private val sendUserUpdateEvent: sendUserUpdateEvent,
) {
    private val logger = LoggerFactory.getLogger(OutboxSchedule::class.java)

    @Scheduled(fixedRate = 5000)
    fun eventProcessor() {
        val events = outboxRepository.findTop10ByStatus(OutboxEventStatus.PENDING)

        events.forEach { event ->
            try {
                logger.info("Sending event: {}", event.payload)

                when (event.eventType) {
                    "SEND_SAVE_PURCHASE_EVENT" -> sendSavePurchaseEvent.sendToKafkaTopic(event.payload)
                    "SEND_ITEM_STATUS_EVENT" -> sendItemStatusEvent.sendToKafkaTopic(event.payload)
                    "SEND_USER_UPDATE_EVENT" -> sendUserUpdateEvent.sendToKafkaTopic(event.payload)
                    else -> logger.warn("Unknow event type: {}", event.eventType)
                }

                event.status = OutboxEventStatus.SENT
                outboxRepository.save(event)
            }catch (ex: Exception){
                logger.error("Failure sending event:{}: {}", event.id, ex.message)
            }
        }
    }
}