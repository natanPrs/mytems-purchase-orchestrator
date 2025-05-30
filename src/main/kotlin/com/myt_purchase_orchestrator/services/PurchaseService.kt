package com.myt_purchase_orchestrator.services

import com.myt_purchase_orchestrator.dtos.PurchaseDto
import com.myt_purchase_orchestrator.dtos.SavePurchaseDto
import com.myt_purchase_orchestrator.mappers.toEventUpdateStatus
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class PurchaseService(
    private val userValidationClientService: UserValidationClientService,
    private val itemValidationClientService: ItemValidationClientService,
    private val kafkaTemplate: KafkaTemplate<String, SavePurchaseDto>) {

    fun realizePurchase(purchaseDto: PurchaseDto){
        val validateUser = validateUser(purchaseDto)
        val validateItem = validateItem(purchaseDto)

        val eventUpdateItemStatus = purchaseDto.toEventUpdateStatus()

        if (validateItem && validateUser){
            kafkaTemplate.send("topic", eventUpdateItemStatus)
        }
    }

    fun validateUser(purchaseDto: PurchaseDto): Boolean =
        userValidationClientService.sendUsersToValidate(purchaseDto)

    fun validateItem(purchaseDto: PurchaseDto): BigDecimal? =
        itemValidationClientService.sendItemToValidate(purchaseDto)
}