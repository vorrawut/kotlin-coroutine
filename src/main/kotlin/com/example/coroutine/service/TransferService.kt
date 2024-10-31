package com.example.coroutine.service

import com.example.coroutine.model.TransferRequest
import com.example.coroutine.model.TransferResponse
import com.example.coroutine.repository.AccountRepository
import kotlinx.coroutines.delay
import org.springframework.stereotype.Service
import java.util.*

@Service
class TransferService(private val accountRepository: AccountRepository) {

    suspend fun processTransfer(request: TransferRequest): TransferResponse {
        return try {
            delay(2000)  // Simulate a 2-second delay to "process" the transfer

            // Step 1: Validate the accounts exist
            val fromBalance = accountRepository.getBalance(request.fromAccount)
                ?: return TransferResponse("failed", "Account ${request.fromAccount} not found.")
            val toBalance = accountRepository.getBalance(request.toAccount)
                ?: return TransferResponse("failed", "Account ${request.toAccount} not found.")

            // Step 2: Check for sufficient funds
            if (fromBalance < request.amount) {
                return TransferResponse("failed", "Insufficient funds in account ${request.fromAccount}.")
            }

            // Step 3: Perform the transfer
            accountRepository.updateBalance(request.fromAccount, fromBalance - request.amount)
            accountRepository.updateBalance(request.toAccount, toBalance + request.amount)

            // Step 4: Create a transaction ID
            val transactionId = UUID.randomUUID().toString()
            TransferResponse("success", "Transfer completed successfully", transactionId)
        } catch (e: Exception) {
            TransferResponse("failed", e.message ?: "Unexpected error")
        }
    }
}
