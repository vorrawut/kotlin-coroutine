package com.example.coroutine.repository

interface AccountRepository {
    fun getBalance(accountId: String): Double?
    fun updateBalance(accountId: String, newBalance: Double)
}