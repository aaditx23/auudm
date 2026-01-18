package com.aaditx23.auudm.data.local.mapper

import com.aaditx23.auudm.data.local.entity.ReceiptEntity
import com.aaditx23.auudm.domain.model.Receipt

fun Receipt.toEntity() = ReceiptEntity(
    id = id,
    receiptNo = receiptNo,
    donorName = donorName,
    address = address,
    month = month,
    amount = amount,
    recipientName = recipientName,
    recipientDesignation = recipientDesignation,
    medium = medium,
    mediumReference = mediumReference,
    date = date
)

fun ReceiptEntity.toDomain() = Receipt(
    id = id,
    receiptNo = receiptNo,
    donorName = donorName,
    address = address,
    month = month,
    amount = amount,
    recipientName = recipientName,
    recipientDesignation = recipientDesignation,
    medium = medium,
    mediumReference = mediumReference,
    date = date
)
