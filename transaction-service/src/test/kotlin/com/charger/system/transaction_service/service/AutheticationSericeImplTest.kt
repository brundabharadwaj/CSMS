

    package com.charger.system.transaction_service.service

    import AuthorizeRequest
    import DriverIdentifier
    import com.charger.system.transaction_service.model.*
    import com.charger.system.transaction_service.service.kafka.producer.ProducerService
    import io.mockk.*
    import org.apache.kafka.common.errors.TimeoutException
    import org.junit.jupiter.api.BeforeEach
    import org.junit.jupiter.api.Test
    import org.junit.jupiter.api.assertThrows
    import org.junit.jupiter.params.ParameterizedTest
    import org.junit.jupiter.params.provider.Arguments
    import org.junit.jupiter.params.provider.MethodSource
    import java.util.*
    import java.util.concurrent.CompletableFuture
    import java.util.concurrent.TimeUnit
    import java.util.stream.Stream
    import kotlin.test.assertEquals

    class AuthenticationServiceImplTest {

        private lateinit var authenticationService: AuthenticationServiceImpl
        private val producerService: ProducerService = mockk()
        private val responseStore: ResponseStore = mockk()

        @BeforeEach
        fun setUp() {
            authenticationService = AuthenticationServiceImpl(producerService, responseStore)
        }

        @Test
        fun `test authenticate success`() {
            val correlationId = UUID.randomUUID().toString()
            val request = AuthorizeRequest(UUID.randomUUID(), DriverIdentifier(UUID.randomUUID()))

            val future = mockk<CompletableFuture<AuthenticationStatus>>()
            every { responseStore.createFuture(correlationId) } returns future
            every { future.get(5, TimeUnit.SECONDS) } returns AuthenticationStatus.ACCEPTED
            every { producerService.sendMessage(any()) } just Runs

            mockkStatic(UUID::class)
            every { UUID.randomUUID().toString() } returns correlationId

            // Act
            val result = authenticationService.authenticate(request)

            // Assert
            assertEquals(AuthenticationStatus.ACCEPTED, result)
        }



            companion object {
                @JvmStatic
                fun provideFailureCases(): Stream<Arguments> = Stream.of(
                    Arguments.of(TimeoutException("Future timed out"), "Authentication timed out"),
                    Arguments.of(RuntimeException(), "Authentication failed")
                )
            }
            @ParameterizedTest
            @MethodSource(value = ["provideFailureCases"])
            fun `test authenticate should throw relevant Exceptions`(exception: Exception, expectedMessage: String) {
                val correlationId = UUID.randomUUID().toString()
                val request = AuthorizeRequest(UUID.randomUUID(), DriverIdentifier(UUID.randomUUID()))

                val future = mockk<CompletableFuture<AuthenticationStatus>>()

                mockkStatic(UUID::class)
                every { UUID.randomUUID().toString() } returns correlationId
                every { responseStore.createFuture(correlationId) } returns future
                every { producerService.sendMessage(any()) } just Runs
                every { future.get(5, TimeUnit.SECONDS) } throws exception

                val thrownException = assertThrows<RuntimeException> {
                    authenticationService.authenticate(request)
                }

                assertEquals(expectedMessage, thrownException.message)
            }



    }