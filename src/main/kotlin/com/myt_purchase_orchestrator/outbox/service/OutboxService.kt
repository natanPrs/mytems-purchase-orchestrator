package com.myt_purchase_orchestrator.outbox.service

import com.myt_purchase_orchestrator.outbox.model.OutboxEventModel
import com.myt_purchase_orchestrator.outbox.repository.OutboxRepository
import org.springframework.stereotype.Service

@Service
class OutboxService(
    private val outboxRepository: OutboxRepository,
    ) {
    fun registerEvent(eventType: String, payload: String) {

        val event = OutboxEventModel(
            eventType = eventType,
            payload = payload
        )

        outboxRepository.save(event)
    }
}