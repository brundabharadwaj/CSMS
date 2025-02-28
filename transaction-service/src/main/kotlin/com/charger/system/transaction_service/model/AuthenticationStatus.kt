package com.charger.system.transaction_service.model

enum class AuthenticationStatus(val message: String) {
    ACCEPTED("Accepted"),
    INVALID("Invalid"),
    UNKNOWN("Unknown"),
    REJECTED("Rejected");

    override fun toString(): String {
        return message
    }
}
