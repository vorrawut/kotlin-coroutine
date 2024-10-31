package com.example.coroutine.controller

import com.example.coroutine.model.TransferRequest
import com.example.coroutine.model.TransferResponse
import com.example.coroutine.service.TransferService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TransferController(private val transferService: TransferService) {

    @PostMapping("/transfer")
    suspend fun makeTransfer(@RequestBody request: TransferRequest): ResponseEntity<TransferResponse> {
        val response = transferService.processTransfer(request)

        return if (response.status == "success") {
            // If successful, return a 200 OK with the transfer details
            ResponseEntity.ok(response)
        } else {
            // If something goes wrong, return a 400 BAD REQUEST
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }
    }
}