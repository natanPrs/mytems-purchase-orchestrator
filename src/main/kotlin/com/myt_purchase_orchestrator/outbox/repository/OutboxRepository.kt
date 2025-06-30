package com.myt_purchase_orchestrator.outbox.repository

import com.myt_purchase_orchestrator.enums.OutboxEventStatus
import com.myt_purchase_orchestrator.outbox.model.OutboxEventModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OutboxRepository : JpaRepository<OutboxEventModel, UUID> {
    fun findTop10ByStatus(status: OutboxEventStatus): List<OutboxEventModel>
}