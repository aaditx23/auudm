package com.aaditx23.auudm.data.remote.model

data class ReceiptDto(
    var id: String = "",
    var donorName: String = "",
    var address: String = "",
    var month: List<Int> = emptyList(),
    var year: Int = 0,
    var amount: Double = 0.0,
    var recipientIndex: Int = 0,
    var medium: Int = 0,
    var mediumReference: String = "",
    var date: Long = 0L,
    var createdAt: Long = 0L,
    var isSynced: Boolean = false
)
