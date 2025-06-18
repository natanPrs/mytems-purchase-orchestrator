package com.myt_purchase_orchestrator.services

import com.myt_purchase_orchestrator.dtos.PurchaseDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.UUID


@FeignClient(name = "user-validation-service", url = "http://localhost:8081/validate-users")
interface UserValidationClientService {

    @PostMapping("/purchase-validate-user")
    fun sendUsersToValidate(@RequestBody purchaseDto: PurchaseDto): UUID?
}