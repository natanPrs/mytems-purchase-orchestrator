package com.myt_purchase_orchestrator.outbox

import com.myt_purchase_orchestrator.enums.OutboxEventStatus
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OutboxJob(
    private val outboxRepository: OutboxRepository
) {
    private val logger = LoggerFactory.getLogger(OutboxJob::class.java)

    @Scheduled(fixedRate = 5000)
    fun eventProcessor() {
        val events = outboxRepository.findTop10ByStatus(OutboxEventStatus.PENDING)

        events.forEach { event ->
            try {
                logger.info("Sending event: {}", event.payload)



                event.status = OutboxEventStatus.SENT
                outboxRepository.save(event)
            }catch (ex: Exception){
                logger.error("Failure sending event:{}: {}", event.id, ex.message)
            }
        }
    }
}