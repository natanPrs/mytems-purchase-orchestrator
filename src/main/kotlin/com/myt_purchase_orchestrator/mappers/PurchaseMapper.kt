package com.myt_purchase_orchestrator.mappers

import com.myt_purchase_orchestrator.dtos.EventUpdateItemStatusDto
import com.myt_purchase_orchestrator.dtos.PurchaseDto

fun PurchaseDto.toEventUpdateStatus(): EventUpdateItemStatusDto =
    EventUpdateItemStatusDto(
        globalItemId = this.globalItemId,
        newStatus = "SOLD",
    )