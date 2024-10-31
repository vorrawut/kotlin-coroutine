package com.example.coroutine.service

import com.example.coroutine.model.Product
import kotlinx.coroutines.delay
import org.springframework.stereotype.Service

@Service
class ProductService {

    // Simulate a "database" of products
    private val products = listOf(
        Product("1", "Super Cool Laptop", "A high-performance laptop for developers and gamers.", 1500.00),
        Product("2", "Wireless Headphones", "Noise-canceling headphones for distraction-free focus.", 300.00),
        Product("3", "Smartwatch", "Track your fitness and notifications on the go.", 200.00)
    )

    // Simulate fetching product details (this could be from a database or external API)
    suspend fun getProductById(id: String): Product {
        delay(1000)  // Simulate a 1-second "slow" database or API call

        // Try to find the product by ID; throw an exception if not found
        return products.find { it.id == id }
            ?: throw Exception("Product not found!")
    }
}