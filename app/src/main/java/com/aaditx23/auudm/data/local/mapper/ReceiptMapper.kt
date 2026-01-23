package com.aaditx23.auudm.data.local.mapper

import com.aaditx23.auudm.data.local.entity.ReceiptEntity
import com.aaditx23.auudm.domain.model.Receipt

fun Receipt.toEntity() = ReceiptEntity(
    id = id,
    donorName = donorName,
    address = address,
    month = month,
    amount = amount,
    recipientIndex = recipientIndex,
    medium = medium,
    mediumReference = mediumReference,
    date = date,
    isSynced = isSynced
)

fun ReceiptEntity.toDomain() = Receipt(
    id = id,
    donorName = donorName,
    address = address,
    month = month,
    amount = amount,
    recipientIndex = recipientIndex,
    medium = medium,
    mediumReference = mediumReference,
    date = date,
    isSynced = isSynced
)
