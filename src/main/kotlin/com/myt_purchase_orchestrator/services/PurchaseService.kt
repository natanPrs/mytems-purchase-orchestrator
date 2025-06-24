package com.myt_purchase_orchestrator.services

import com.myt_purchase_orchestrator.dtos.PurchaseDto
import com.myt_purchase_orchestrator.dtos.EventSavePurchaseDto
import com.myt_purchase_orchestrator.dtos.EventUpdateItemStatusDto
import com.myt_purchase_orchestrator.dtos.EventUpdateUserDto
import com.myt_purchase_orchestrator.mappers.toEventUpdateStatus
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
    private val purchaseKafkaProducer: PurchaseKafkaProducer){

    private val logger = LoggerFactory.getLogger(PurchaseService::class.java)

    fun realizePurchase(purchaseDto: PurchaseDto){
        try {
            logger.info("Processing purchase for buyerId={} sellerId{}, globalItemId{}",
                purchaseDto.buyerId, purchaseDto.sellerId, purchaseDto.globalItemId)

            val validateUserAndGetSellerId = validateUser(purchaseDto)
            val validateItemAndGetAmount = validateItem(purchaseDto)

            purchaseKafkaProducer.sendSavePurchaseEvent(purchaseDto, validateUserAndGetSellerId, validateItemAndGetAmount)
            purchaseKafkaProducer.sendItemStatusEvent(purchaseDto)
            purchaseKafkaProducer.sendUserUpdateEvent(validateItemAndGetAmount, purchaseDto.buyerId,purchaseDto.globalItemId)

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




}