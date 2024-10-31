package com.example.coroutine.model

data class TransferResponse(
    val status: String,
    val message: String,
    val transactionId: String? = null
)