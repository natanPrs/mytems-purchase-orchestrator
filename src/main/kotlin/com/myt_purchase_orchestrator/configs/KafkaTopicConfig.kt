package com.myt_purchase_orchestrator.configs

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaTopicConfig {

    fun updateUserTopic(): NewTopic =
        NewTopic("updateUser", 3,2)

    fun updateItemStatusTopic(): NewTopic =
        NewTopic("updateItemStatus", 3, 1)

    fun savePurchaseTopic(): NewTopic =
        NewTopic("savePurchase", 3,2)
}