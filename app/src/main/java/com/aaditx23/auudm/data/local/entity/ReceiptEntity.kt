package com.aaditx23.auudm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receipts")
data class ReceiptEntity(
    @PrimaryKey(autoGenerate = true)
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
