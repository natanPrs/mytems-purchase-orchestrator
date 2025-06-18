package com.myt_purchase_orchestrator.dtos

import java.math.BigDecimal
import java.util.UUID

data class EventUpdateUserDto(
    val amount: BigDecimal,
    val buyerId: UUID,
    val globalItemId: UUID,
)
