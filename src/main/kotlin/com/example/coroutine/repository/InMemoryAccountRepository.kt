package com.example.coroutine.repository

import org.springframework.stereotype.Repository

@Repository
class InMemoryAccountRepository : AccountRepository {
    private val accountBalances = mutableMapOf(
        "12345" to 500.0,
        "67890" to 2000.0
    )

    override fun getBalance(accountId: String): Double? = accountBalances[accountId]

    override fun updateBalance(accountId: String, newBalance: Double) {
        accountBalances[accountId] = newBalance
    }
}
