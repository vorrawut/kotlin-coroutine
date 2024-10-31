package com.example.coroutine.controller

import com.example.coroutine.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(private val productService: ProductService) {

    @GetMapping("/products/{id}")
    suspend fun getProductDetails(@PathVariable id: String): ResponseEntity<Any> {
        return try {
            // Fetch product details asynchronously (non-blocking, yay!)
            val product = productService.getProductById(id)
            ResponseEntity.ok(product)  // Return the product details with a 200 OK
        } catch (e: Exception) {
            // Handle the case where the product wasn't found
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found")        }
    }
}