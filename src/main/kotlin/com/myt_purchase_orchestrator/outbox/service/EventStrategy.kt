package com.myt_purchase_orchestrator.outbox.service

interface EventStrategy {
    fun sendToKafkaTopic(payload: String)
}