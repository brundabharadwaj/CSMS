package com.charger.system.transaction_service.model

import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap

@Component
class ResponseStore {
    private val pendingResponses = ConcurrentHashMap<String, CompletableFuture<AuthenticationStatus>>()  // Stores pending responses

    fun createFuture(correlationId: String): CompletableFuture<AuthenticationStatus> {
        val future = CompletableFuture<AuthenticationStatus>()
        pendingResponses[correlationId] = future
        return future
    }

    fun completeFuture(correlationId: String, authStatus: AuthenticationStatus) {
        pendingResponses.remove(correlationId)?.complete(authStatus)
    }
}
