package com.charger.system.transaction_service.api;


import com.charger.system.transaction_service.model.AuthenticationStatus
import com.charger.system.transaction_service.model.AuthorizationResponse
import com.charger.system.transaction_service.service.AuthenticationServiceImpl
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.stream.Stream
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test


@WebMvcTest(AuthorizationController::class)
class AuthorizationControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var authenticationService: AuthenticationServiceImpl

    companion object {

        @JvmStatic
        fun invalidRequestBodies(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    """{
                        "stationUuid": "550e8400-e29b-41d4-a716-446655440000",
                        "driverIdentifier": {
                          "id": "testIDC"
                        }
                      }"""
                ),
                Arguments.of(
                    """{
                        "test": "550e8400-e29b-41d4-a716-446655440000",
                        "test": { }
                      }"""
                ),
                Arguments.of(
                    """{
                        "stationUuid": "550e8400-e29b-41d4-a716-446655440000",
                        "driverId": {
                          "id": "testIDC"
                        }
                      }"""
                )
            )
        }
    }

    @Test
    fun `Success scenario - valid authorization request`() {
        val requestJson = """
        {
          "stationUuid": "550e8400-e29b-41d4-a716-446655440000",
          "driverIdentifier": {
            "id": "550e8400-e29b-41d4-a716-446655440111"
          }
        }
    """.trimIndent()

        // Mock service response
        val mockResponse = AuthorizationResponse("AUTHORIZED")
        every { authenticationService.authenticate(any()) } returns AuthenticationStatus.ACCEPTED
        every { authenticationService.authorisation(any(), any()) } returns mockResponse

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("AUTHORIZED"))
    }
    @Test
    fun `Failure scenario - Internal Server Error`() {
        val requestJson = """
        {
          "stationUuid": "550e8400-e29b-41d4-a716-446655440000",
          "driverIdentifier": {
            "id": "550e8400-e29b-41d4-a716-446655440111"
          }
        }
    """.trimIndent()

        // Mock service response
        val mockResponse = AuthorizationResponse("AUTHORIZED")
        every { authenticationService.authenticate(any()) } throws RuntimeException("Authentication failed")
        every { authenticationService.authorisation(any(), any()) } returns mockResponse

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(MockMvcResultMatchers.status().isInternalServerError)

    }


    @ParameterizedTest
        @MethodSource("invalidRequestBodies")
        fun `Failure scenario - invalid request should return 400`(requestJson: String) {
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/authorize")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson)
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
        }

}
