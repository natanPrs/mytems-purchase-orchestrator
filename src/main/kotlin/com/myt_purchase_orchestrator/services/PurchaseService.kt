package com.myt_purchase_orchestrator.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.myt_purchase_orchestrator.dtos.PurchaseDto
import com.myt_purchase_orchestrator.dtos.EventSavePurchaseDto
import com.myt_purchase_orchestrator.dtos.EventUpdateItemStatusDto
import com.myt_purchase_orchestrator.dtos.EventUpdateUserDto
import com.myt_purchase_orchestrator.mappers.toEventUpdateStatus
import com.myt_purchase_orchestrator.outbox.service.OutboxService
import com.myt_purchase_orchestrator.producers.PurchaseKafkaProducer
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import kotlin.math.log

@Service
class PurchaseService(
    private val userValidationClientService: UserValidationClientService,
    private val itemValidationClientService: ItemValidationClientService,
    private val outboxService: OutboxService,
    private val objectMapper: ObjectMapper){

    private val logger = LoggerFactory.getLogger(PurchaseService::class.java)

    fun realizePurchase(purchaseDto: PurchaseDto){
        try {
            logger.info("Processing purchase for buyerId={} sellerId{}, globalItemId{}",
                purchaseDto.buyerId, purchaseDto.sellerId, purchaseDto.globalItemId)

            val validateUserAndGetSellerId = validateUser(purchaseDto)
            val validateItemAndGetAmount = validateItem(purchaseDto)

            sendSavePurchaseEvent(purchaseDto, validateUserAndGetSellerId, validateItemAndGetAmount)
            outboxService.registerEvent("SEND_ITEM_STATUS_EVENT", objectMapper.writeValueAsString(purchaseDto))
            sendUserUpdateEvent(validateItemAndGetAmount, purchaseDto.buyerId,purchaseDto.globalItemId)

            logger.info("Purchase successfully completed for item {}", purchaseDto.globalItemId)
        } catch (ex: IllegalStateException){
            logger.error("Validate error ex{}", ex.message)
            throw ex
        } catch (ex: Exception){
            logger.error("Unexpected error during purchase processing ex{}", ex.message)
            throw RuntimeException("Failure during purchase processing, try again.")
        }
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
            localStamp = LocalDateTime.now()).let {
                objectMapper.writeValueAsString(it)
        }

        outboxService.registerEvent("SEND_SAVE_PURCHASE_EVENT", event)
    }

    fun sendUserUpdateEvent(amount: BigDecimal, buyerId : UUID, itemId: UUID){
        val event = EventUpdateUserDto(amount, buyerId ,itemId).let {
            objectMapper.writeValueAsString(it)
        }
        outboxService.registerEvent("SEND_USER_UPDATE_EVENT",event)
    }
}