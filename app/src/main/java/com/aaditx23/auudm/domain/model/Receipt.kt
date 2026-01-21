package com.aaditx23.auudm.domain.model

import java.util.UUID

data class Receipt(
    val id: String = UUID.randomUUID().toString(),
    val donorName: String,
    val address: String,
    val month: Int,
    val amount: Double,
    val recipientName: String,
    val recipientDesignation: String,
    val medium: Int,
    val mediumReference: String,
    val date: Long,
    val isSynced: Boolean = false
)
