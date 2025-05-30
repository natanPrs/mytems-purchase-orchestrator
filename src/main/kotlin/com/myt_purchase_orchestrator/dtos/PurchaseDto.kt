package com.myt_purchase_orchestrator.dtos

import java.util.UUID

data class PurchaseDto(
    val buyerId: UUID,
    val sellerId: UUID,
    val itemId: UUID,
)
