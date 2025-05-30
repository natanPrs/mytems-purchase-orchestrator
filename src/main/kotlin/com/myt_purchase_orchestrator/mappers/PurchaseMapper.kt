package com.myt_purchase_orchestrator.mappers

import com.myt_purchase_orchestrator.dtos.EventUpdateItemStatus
import com.myt_purchase_orchestrator.dtos.PurchaseDto

fun PurchaseDto.toEventUpdateStatus(): EventUpdateItemStatus =
    EventUpdateItemStatus(
        itemId = this.itemId,
        newStatus = "SOLD",
    )