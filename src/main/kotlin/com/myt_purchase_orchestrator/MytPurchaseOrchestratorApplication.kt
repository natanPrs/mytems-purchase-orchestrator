package com.myt_purchase_orchestrator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class MytPurchaseOrchestratorApplication

fun main(args: Array<String>) {
	runApplication<MytPurchaseOrchestratorApplication>(*args)
}

