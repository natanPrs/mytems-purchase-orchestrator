package com.myt_purchase_orchestrator.dtos

import java.util.UUID

data class EventUpdateItemStatus(
    val itemId: UUID,
    val newStatus: String,
)
