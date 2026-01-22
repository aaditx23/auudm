package com.aaditx23.auudm.data.remote.model

import com.google.firebase.firestore.PropertyName

data class ReceiptDto(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("donorName")
    @set:PropertyName("donorName")
    var donorName: String = "",

    @get:PropertyName("address")
    @set:PropertyName("address")
    var address: String = "",

    @get:PropertyName("month")
    @set:PropertyName("month")
    var month: Int = 0,

    @get:PropertyName("amount")
    @set:PropertyName("amount")
    var amount: Double = 0.0,

    @get:PropertyName("recipientName")
    @set:PropertyName("recipientName")
    var recipientName: String = "",

    @get:PropertyName("recipientDesignation")
    @set:PropertyName("recipientDesignation")
    var recipientDesignation: String = "",

    @get:PropertyName("medium")
    @set:PropertyName("medium")
    var medium: Int = 0,

    @get:PropertyName("mediumReference")
    @set:PropertyName("mediumReference")
    var mediumReference: String = "",

    @get:PropertyName("date")
    @set:PropertyName("date")
    var date: Long = 0L
)

