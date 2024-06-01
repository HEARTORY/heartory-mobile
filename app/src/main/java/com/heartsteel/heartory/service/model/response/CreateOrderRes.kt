package com.heartsteel.heartory.service.model.response

data class CreateOrderRes (
    val bin: String,
    val accountNumber: String,
    val accountName: String,
    val amount: Int,
    val description: String,
    val orderCode: Int,
    val currency: String,
    val paymentLinkId: String,
    val status: String,
    val checkoutUrl: String,
    val qrCode: String
)