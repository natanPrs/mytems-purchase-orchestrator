package com.myt_purchase_orchestrator.services

import com.myt_purchase_orchestrator.dtos.PurchaseDto
import com.myt_purchase_orchestrator.dtos.EventSavePurchaseDto
import com.myt_purchase_orchestrator.dtos.EventUpdateItemStatusDto
import com.myt_purchase_orchestrator.dtos.EventUpdateUserDto
import com.myt_purchase_orchestrator.mappers.toEventUpdateStatus
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Service
class PurchaseService(
    private val userValidationClientService: UserValidationClientService,
    private val itemValidationClientService: ItemValidationClientService,
    private val eventUpdateItemStatusDtoTemplate: KafkaTemplate<String, EventUpdateItemStatusDto>,
    private val eventUpdateUserTemplate: KafkaTemplate<String, EventUpdateUserDto>,
    private val eventSavePurchaseTemplate: KafkaTemplate<String, EventSavePurchaseDto>) {

    fun realizePurchase(purchaseDto: PurchaseDto){
        val validateUserAndGetSellerId = validateUser(purchaseDto)
        val validateItemAndGetAmount = validateItem(purchaseDto)

        sendSavePurchaseEvent(purchaseDto, validateUserAndGetSellerId, validateItemAndGetAmount)
        sendItemStatusEvent(purchaseDto)
        sendUserUpdateEvent(validateItemAndGetAmount, purchaseDto.buyerId,purchaseDto.globalItemId)
    }

    private fun validateUser(purchaseDto: PurchaseDto): UUID =
        userValidationClientService.sendUsersToValidate(purchaseDto) ?: throw Exception("The User return was Null.")

    private fun validateItem(purchaseDto: PurchaseDto): BigDecimal =
        itemValidationClientService.sendItemToValidate(purchaseDto) ?: throw Exception("The Item return was Null.")

    private fun sendSavePurchaseEvent(purchaseDto: PurchaseDto, sellerId: UUID, amount: BigDecimal){
        val event = EventSavePurchaseDto(
            globalItemId = purchaseDto.globalItemId,
            sellerId = sellerId,
            buyerId = purchaseDto.buyerId,
            amount = amount,
            localStamp = LocalDateTime.now())

        eventSavePurchaseTemplate.send("savePurchase", event)
    }

    private fun sendItemStatusEvent(purchaseDto: PurchaseDto){
        val event = purchaseDto.toEventUpdateStatus()
        eventUpdateItemStatusDtoTemplate.send("updateItemStatus", event)
    }

    private fun sendUserUpdateEvent(amount: BigDecimal, buyerId : UUID, itemId: UUID){
        val event = EventUpdateUserDto(amount, buyerId ,itemId)
        eventUpdateUserTemplate.send("updateUser", event)
    }
}