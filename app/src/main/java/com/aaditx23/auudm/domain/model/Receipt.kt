package com.aaditx23.auudm.domain.model

data class Receipt(
    val id: Long = 0,
    val receiptNo: String,
    val donorName: String,
    val address: String,
    val month: String,
    val amount: Double,
    val recipientName: String,
    val recipientDesignation: String,
    val medium: String,
    val mediumReference: String,
    val date: Long
)
