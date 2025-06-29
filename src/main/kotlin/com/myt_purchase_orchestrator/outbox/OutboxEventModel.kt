package com.myt_purchase_orchestrator.outbox

import com.myt_purchase_orchestrator.enums.OutboxEventStatus
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "outbox")
data class OutboxEventModel(

    @Id
    val id: UUID = UUID.randomUUID(),

    val eventType: String,

    val payload: String,

    var status: OutboxEventStatus = OutboxEventStatus.PENDING,

    val localStamp: LocalDateTime = LocalDateTime.now()
) {
}