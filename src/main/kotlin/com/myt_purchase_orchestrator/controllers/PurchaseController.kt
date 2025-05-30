package com.myt_purchase_orchestrator.controllers

import com.myt_purchase_orchestrator.dtos.PurchaseDto
import com.myt_purchase_orchestrator.services.PurchaseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/purchase")
class PurchaseController(
    private val purchaseService: PurchaseService) {

    @PostMapping
    fun receivePurchase(@RequestBody purchaseDto: PurchaseDto): ResponseEntity<String>{
        purchaseService.realizePurchase(purchaseDto)
        return ResponseEntity("EBAA", HttpStatus.OK)
    }
}