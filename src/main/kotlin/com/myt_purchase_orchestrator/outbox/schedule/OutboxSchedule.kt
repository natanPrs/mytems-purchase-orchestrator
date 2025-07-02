package com.myt_purchase_orchestrator.outbox.schedule

import com.myt_purchase_orchestrator.enums.OutboxEventStatus
import com.myt_purchase_orchestrator.outbox.repository.OutboxRepository
import com.myt_purchase_orchestrator.outbox.service.EventStrategyFactory
import com.myt_purchase_orchestrator.outbox.service.SendItemStatusEvent
import com.myt_purchase_orchestrator.outbox.service.SendSavePurchaseEvent
import com.myt_purchase_orchestrator.outbox.service.SendUserUpdateEvent
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OutboxSchedule(
    private val outboxRepository: OutboxRepository,
    private val eventStrategyFactory: EventStrategyFactory,
) {
    private val logger = LoggerFactory.getLogger(OutboxSchedule::class.java)

    @Scheduled(fixedRate = 5000)
    fun eventProcessor() {
        val events = outboxRepository.findTop10ByStatus(OutboxEventStatus.PENDING)

        events.forEach { event ->
            try {
                logger.info("Sending event: {}", event.payload)

                val eventType = eventStrategyFactory.createEvent(event.eventType)

                eventType.sendToKafkaTopic(event.payload)

                event.status = OutboxEventStatus.SENT
                outboxRepository.save(event)
            }catch (ex: Exception){
                logger.error("Failure sending event:{}: {}", event.id, ex.message)
            }
        }
    }
}