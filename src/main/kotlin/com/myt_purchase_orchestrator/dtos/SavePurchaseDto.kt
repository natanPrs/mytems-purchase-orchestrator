package com.myt_purchase_orchestrator.dtos

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class SavePurchaseDto(
    val itemId: UUID,

    val sellerId: UUID,

    val buyerId: UUID,

    val amount: BigDecimal,

    val localStamp: LocalDateTime
)
