package com.example.coroutine.service

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private lateinit var productService: ProductService

    @Test
    fun `should return product details when product is found`() = runBlocking {
        // Act
        val product = productService.getProductById("1")

        // Assert
        assertNotNull(product)
        assertEquals("1", product.id)
        assertEquals("Super Cool Laptop", product.name)
        assertEquals("A high-performance laptop for developers and gamers.", product.description)
        assertEquals(1500.00, product.price)
    }

    @Test
    fun `should throw exception when product is not found`() = runBlocking {
        // Act & Assert
        val exception = assertThrows<Exception> {
            runBlocking { productService.getProductById("999") }
        }
        assertEquals("Product not found!", exception.message)
    }
}
