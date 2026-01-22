package com.aaditx23.auudm.data.remote.model

data class ReceiptDto(
    var id: String = "",
    var donorName: String = "",
    var address: String = "",
    var month: Int = 0,
    var amount: Double = 0.0,
    var recipientName: String = "",
    var recipientDesignation: String = "",
    var medium: Int = 0,
    var mediumReference: String = "",
    var date: Long = 0L,
    var isSynced: Boolean = false
)

