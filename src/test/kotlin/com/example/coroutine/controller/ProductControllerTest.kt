package com.example.coroutine.controller

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return product details when product is found`(): Unit = runBlocking {
        mockMvc.get("/products/1")
            .andExpect {
                status().isOk
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.id").value("1")
                jsonPath("$.name").value("Super Cool Laptop")
                jsonPath("$.description").value("A high-performance laptop for developers and gamers.")
                jsonPath("$.price").value(1500.00)
            }
    }

    @Test
    fun `should return 404 when product is not found`(): Unit = runBlocking {
        mockMvc.get("/products/999")
            .andExpect {
                status().isNotFound
                content().string("Product not found")
            }
    }
}
