package com.aaditx23.auudm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "receipts")
data class ReceiptEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val donorName: String,
    val address: String,
    val month: Int,
    val amount: Double,
    val recipientIndex: Int,
    val medium: Int,
    val mediumReference: String,
    val date: Long,
    val isSynced: Boolean = false
)
