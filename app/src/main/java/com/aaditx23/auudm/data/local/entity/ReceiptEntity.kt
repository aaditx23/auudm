package com.aaditx23.auudm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receipts")
data class ReceiptEntity(
    @PrimaryKey(autoGenerate = true)
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
