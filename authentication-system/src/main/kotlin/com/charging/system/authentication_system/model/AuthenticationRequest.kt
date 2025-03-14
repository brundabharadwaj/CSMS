import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

data class AuthenticationRequest(
    val stationUuid: UUID,
    val driverIdentifier: DriverIdentifier
)

data class DriverIdentifier(
    val id: UUID
)
data class KafkaAuthRequest(
    val correlationId: UUID,
    val payload: SecureAuthorizeRequest
)

data class SecureAuthorizeRequest(
    val stationUuid: UUID,
    val driverIdentifier: DriverIdentifier,
    val timestamp: Long,
    val nonce: String,
    val signature: String
) {
    companion object {
        private const val SECRET_KEY = "your-secret-key"  // Replace with a securely stored key

        fun fromAuthorizeRequest(request: AuthenticationRequest): SecureAuthorizeRequest {
            val timestamp = System.currentTimeMillis()
            val nonce = UUID.randomUUID().toString()
            val signature = generateHMACSignature(request, timestamp, nonce)

            return SecureAuthorizeRequest(
                stationUuid = request.stationUuid,
                driverIdentifier = request.driverIdentifier,
                timestamp = timestamp,
                nonce = nonce,
                signature = signature
            )
        }

        private fun generateHMACSignature(request: AuthenticationRequest, timestamp: Long, nonce: String): String {
            val message = "${request.stationUuid}:${request.driverIdentifier.id}:$timestamp:$nonce"
            val hmac = Mac.getInstance("HmacSHA256")
            val secretKeySpec = SecretKeySpec(SECRET_KEY.toByteArray(), "HmacSHA256")
            hmac.init(secretKeySpec)
            val hash = hmac.doFinal(message.toByteArray())
            return Base64.getEncoder().encodeToString(hash)
        }
    }
}