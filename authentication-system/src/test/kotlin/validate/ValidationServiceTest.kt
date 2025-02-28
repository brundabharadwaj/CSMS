package validate

import DriverIdentifier
import SecureAuthorizeRequest
import com.charging.system.authentication_system.service.authentication.AuthenticationService
import com.charging.system.authentication_system.service.validate.ValidationServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*
import java.util.logging.Logger
import kotlin.test.assertTrue

class ValidationServiceTest {

    private val authenticationService = mockk<AuthenticationService>()
    private val logger = mockk<Logger>(relaxed = true)
    private val validationService = ValidationServiceImpl(authenticationService)

    @Test
    fun `isAuthentic should return false for a invalid request`() {
        val stationUuid = UUID.randomUUID()
        val driverId = UUID.randomUUID()
        val timestamp = System.currentTimeMillis()
        val nonce = "unique-nonce"
        val request = SecureAuthorizeRequest(
            stationUuid = stationUuid,
            driverIdentifier = DriverIdentifier(driverId),
            signature = "valid-signature",
            timestamp = timestamp,
            nonce = nonce
        )

        every { authenticationService.findByStationIdAndDriverId(stationUuid, driverId) } returns mockk()

        // Act
        val result = validationService.isAuthentic(request)

        // Assert
        assertTrue(!result)
    }
}