package com.myt_purchase_orchestrator.dtos

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class EventSavePurchaseDto(
    val globalItemId: UUID,

    val sellerId: UUID,

    val buyerId: UUID,

    val amount: BigDecimal,

    val localStamp: LocalDateTime
)
