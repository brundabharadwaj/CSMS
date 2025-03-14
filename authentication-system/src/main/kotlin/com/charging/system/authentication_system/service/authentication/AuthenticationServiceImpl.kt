package com.charging.system.authentication_system.service.authentication

import com.charging.system.authentication_system.entities.AuthenticationEntity
import com.charging.system.authentication_system.repositories.AuthenticationRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthenticationServiceImpl (private val authenticationRepository: AuthenticationRepository): AuthenticationService{
    override fun findByStationIdAndDriverId(stationId: UUID, driverId: UUID) : AuthenticationEntity?{
       return authenticationRepository.findByStationUuidAndDriverId(stationId,driverId)
    }
}