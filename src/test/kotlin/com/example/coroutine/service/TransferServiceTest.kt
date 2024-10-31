package com.example.coroutine.service

import com.example.coroutine.model.TransferRequest
import com.example.coroutine.repository.AccountRepository
import com.example.coroutine.repository.InMemoryAccountRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TransferServiceTest {

    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var transferService: TransferService

    @BeforeEach
    fun setup() {
        accountRepository = spyk(InMemoryAccountRepository())
        transferService = TransferService(accountRepository)
    }

    @Test
    fun `should handle successful transfer`() = runBlocking {
        val request = TransferRequest("12345", "67890", 200.0)
        val response = transferService.processTransfer(request)

        assertEquals("success", response.status)
        coVerify { accountRepository.updateBalance("12345", 300.0) }
        coVerify { accountRepository.updateBalance("67890", 2200.0) }
    }

    @Test
    fun `should handle insufficient funds error`() = runBlocking {
        val request = TransferRequest("12345", "67890", 1000.0)
        val response = transferService.processTransfer(request)

        assertEquals("failed", response.status)
        assertEquals("Insufficient funds in account 12345.", response.message)
    }

    @Test
    fun `should handle non-existent account error`() = runBlocking {
        val request = TransferRequest("99999", "67890", 100.0)
        val response = transferService.processTransfer(request)

        assertEquals("failed", response.status)
        assertEquals("Account 99999 not found.", response.message)
    }

    @Test
    fun `should handle unexpected exception gracefully`() = runBlocking {
        // Simulate an exception when fetching balance for account "12345"
        coEvery { accountRepository.getBalance("12345") } throws RuntimeException("Unexpected database error")

        val request = TransferRequest("12345", "67890", 100.0)
        val response = transferService.processTransfer(request)

        assertEquals("failed", response.status)
        assertEquals("Unexpected database error", response.message)
    }

    @Test
    fun `should handle exception when updating balance gracefully`() = runBlocking {
        // Simulate an exception when updating balance for account "67890"
        coEvery { accountRepository.updateBalance("67890", any()) } throws RuntimeException("Failed to update balance")

        val request = TransferRequest("12345", "67890", 100.0)
        val response = transferService.processTransfer(request)

        assertEquals("failed", response.status)
        assertEquals("Failed to update balance", response.message)
    }
}
