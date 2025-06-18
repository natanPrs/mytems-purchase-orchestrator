package com.myt_purchase_orchestrator.dtos

import java.util.UUID

data class EventUpdateItemStatusDto(
    val globalItemId: UUID,

    val newStatus: String,
)
