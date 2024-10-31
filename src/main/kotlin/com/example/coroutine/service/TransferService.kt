package com.example.coroutine.service

import com.example.coroutine.model.TransferRequest
import com.example.coroutine.model.TransferResponse
import kotlinx.coroutines.delay
import org.springframework.stereotype.Service
import java.util.*

@Service
class TransferService {

    // Fake database of account balances (we're keeping it simple, but it works!)
    private val accountBalances = mutableMapOf(
        "12345" to 500.0,
        "67890" to 2000.0
    )

    // Simulate processing the transfer asynchronously
    suspend fun processTransfer(request: TransferRequest): TransferResponse {
        delay(2000)  // Simulate a 2-second delay to "process" the transfer

        // Step 1: Validate the accounts exist
        if (!accountBalances.containsKey(request.fromAccount)) {
            return TransferResponse("failed", "Account ${request.fromAccount} not found.")
        }
        if (!accountBalances.containsKey(request.toAccount)) {
            return TransferResponse("failed", "Account ${request.toAccount} not found.")
        }

        // Step 2: Check for sufficient funds
        val fromBalance = accountBalances[request.fromAccount]!!
        if (fromBalance < request.amount) {
            return TransferResponse("failed", "Insufficient funds in account ${request.fromAccount}.")
        }

        // Step 3: Perform the transfer
        accountBalances[request.fromAccount] = fromBalance - request.amount
        accountBalances[request.toAccount] = accountBalances[request.toAccount]!! + request.amount

        // Step 4: Create a transaction ID (makes everything feel official, right?)
        val transactionId = UUID.randomUUID().toString()

        return TransferResponse("success", "Transfer completed successfully", transactionId)
    }
}