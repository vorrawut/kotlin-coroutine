package com.example.coroutine.model

data class TransferRequest(
    val fromAccount: String,
    val toAccount: String,
    val amount: Double
)