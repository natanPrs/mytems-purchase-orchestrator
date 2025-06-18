package com.myt_purchase_orchestrator.services

import com.myt_purchase_orchestrator.dtos.PurchaseDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.math.BigDecimal

@FeignClient(name = "item-validation-service", url = "http://localhost:8082/validate-items")
interface ItemValidationClientService {

    @PostMapping("/purchase-validate-item")
    fun sendItemToValidate(@RequestBody purchaseDto: PurchaseDto): BigDecimal?
}