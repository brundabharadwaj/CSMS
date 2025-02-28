package com.charging.system.authentication_system.service.validate

import SecureAuthorizeRequest
import com.charging.system.authentication_system.repositories.AuthenticationRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.util.Base64
import java.util.UUID


@Service
class ValidationServiceImpl(
    private val objectMapper: ObjectMapper,
    private val authRepository: AuthenticationRepository
) : ValidationService {

    companion object {
        private const val SECRET_KEY = "your-secret-key" //TODO: Replace with actual secret from key vaults
        private val usedNonces = mutableSetOf<String>() //TODO: Temporary store (use Redis in production)
        private val logger: Logger = LoggerFactory.getLogger(ValidationServiceImpl::class.java)
    }

    override fun isAuthentic(request: SecureAuthorizeRequest): Boolean {
        val expectedSignature = generateHMACSignature(request)

        return when {
            !isAuthenticated(request.stationUuid, request.driverIdentifier.id) -> {
                logger.warn("Unauthorized: stationUuid or driverId not found in DB!")
                false
            }
            request.signature != expectedSignature -> {
                logger.warn("Signature mismatch!")
                false
            }
            isRequestExpired(request.timestamp) -> {
                logger.warn("Request expired!")
                false
            }
            isNonceUsed(request.nonce) -> {
                logger.warn("Replay attack detected! Nonce already used.")
                false
            }
            else -> true
        }
    }

    private fun isAuthenticated(stationUuid: UUID, driverId: UUID): Boolean {
        val entry = authRepository.findByStationUuidAndDriverId(stationUuid, driverId)
        return entry != null
    }

    private fun generateHMACSignature(request: SecureAuthorizeRequest): String {
        return try {
            val message = "${request.stationUuid}:${request.driverIdentifier.id}:${request.timestamp}:${request.nonce}"

            val hmac = Mac.getInstance("HmacSHA256")
            val secretKeySpec = SecretKeySpec(SECRET_KEY.toByteArray(), "HmacSHA256")
            hmac.init(secretKeySpec)
            val hash = hmac.doFinal(message.toByteArray())

            Base64.getEncoder().encodeToString(hash)
        } catch (e: Exception) {
            logger.error("Error generating HMAC: ${e.message}", e)
            ""
        }
    }

    private fun isRequestExpired(timestamp: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        return (currentTime - timestamp) > 5 * 60 * 1000 // 5-minute validity
    }

    private fun isNonceUsed(nonce: String): Boolean {
        if (usedNonces.contains(nonce)) return true
        usedNonces.add(nonce)
        return false
    }
}
