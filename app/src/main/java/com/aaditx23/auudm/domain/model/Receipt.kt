package com.aaditx23.auudm.domain.model

data class Receipt(
    val id: Long = 0,
    val donorName: String,
    val address: String,
    val month: Int,
    val amount: Double,
    val recipientName: String,
    val recipientDesignation: String,
    val medium: Int,
    val mediumReference: String,
    val date: Long
)
