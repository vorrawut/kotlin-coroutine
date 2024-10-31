package com.example.coroutine

import kotlinx.coroutines.delay
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class DemoController {

    @GetMapping("/hello")
    fun hello(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello, World!")
    }

    @GetMapping("/async")
    suspend fun asyncHello(): ResponseEntity<String> {
        delay(1000)  // Simulate non-blocking delay
        return ResponseEntity.ok("Hello, Async World!")
    }

}
