package com.example.coroutine.controller

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class TransferControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return success when transfer is processed`(): Unit = runBlocking {
        // Act & Assert
        mockMvc.post("/transfer") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "fromAccount": "12345",
                    "toAccount": "67890",
                    "amount": 300.0
                }
            """
        }
            .andExpect {
                status().isOk
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.status").value("success")
                jsonPath("$.message").value("Transfer completed successfully")
                jsonPath("$.transactionId").exists()
            }
    }

    @Test
    fun `should return 400 BAD REQUEST when transfer fails due to insufficient funds`(): Unit = runBlocking {
        // Act & Assert
        mockMvc.post("/transfer") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "fromAccount": "12345",
                    "toAccount": "67890",
                    "amount": 1000.0
                }
            """
        }
            .andExpect {
                status().isBadRequest
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.status").value("failed")
                jsonPath("$.message").value("Insufficient funds in account 12345")
                jsonPath("$.transactionId").doesNotExist()
            }
    }

    @Test
    fun `should return 400 BAD REQUEST when fromAccount does not exist`(): Unit = runBlocking {
        // Act & Assert
        mockMvc.post("/transfer") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "fromAccount": "99999",
                    "toAccount": "67890",
                    "amount": 100.0
                }
            """
        }
            .andExpect {
                status().isBadRequest
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.status").value("failed")
                jsonPath("$.message").value("Account 99999 not found.")
                jsonPath("$.transactionId").doesNotExist()
            }
    }

    @Test
    fun `should return 400 BAD REQUEST when toAccount does not exist`(): Unit = runBlocking {
        // Act & Assert
        mockMvc.post("/transfer") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "fromAccount": "12345",
                    "toAccount": "99999",
                    "amount": 100.0
                }
            """
        }
            .andExpect {
                status().isBadRequest
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.status").value("failed")
                jsonPath("$.message").value("Account 99999 not found.")
                jsonPath("$.transactionId").doesNotExist()
            }
    }
}
