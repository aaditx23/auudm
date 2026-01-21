package com.aaditx23.auudm.data.remote.model

data class ReceiptDto(
    val id: Long = 0,
    val donorName: String = "",
    val address: String = "",
    val month: Int = 0,
    val amount: Double = 0.0,
    val recipientName: String = "",
    val recipientDesignation: String = "",
    val medium: Int = 0,
    val mediumReference: String = "",
    val date: Long = 0L
)

