package com.charging.system.authentication_system.service.authentication

import com.charging.system.authentication_system.repositories.AuthenticationRepository
import java.util.*

class AuthenticationServiceImpl (private val authenticationRepository: AuthenticationRepository): AuthenticationService{
    override fun findByStationIdAndDriverId(stationId: UUID, driverId: UUID) {
        authenticationRepository.findByStationUuidAndDriverId(stationId,driverId)
    }
}