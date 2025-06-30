package com.myt_purchase_orchestrator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
class MytPurchaseOrchestratorApplication

fun main(args: Array<String>) {
	runApplication<MytPurchaseOrchestratorApplication>(*args)
}

