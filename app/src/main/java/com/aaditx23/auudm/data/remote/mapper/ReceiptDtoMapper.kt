package com.aaditx23.auudm.data.remote.mapper

import com.aaditx23.auudm.data.remote.model.ReceiptDto
import com.aaditx23.auudm.domain.model.Receipt

fun Receipt.toDto() = ReceiptDto(
    id = id,
    donorName = donorName,
    address = address,
    month = month,
    amount = amount,
    recipientIndex = recipientIndex,
    medium = medium,
    mediumReference = mediumReference,
    date = date,
    isSynced = true  // Always true when uploading to Firestore
)

fun ReceiptDto.toDomain() = Receipt(
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

