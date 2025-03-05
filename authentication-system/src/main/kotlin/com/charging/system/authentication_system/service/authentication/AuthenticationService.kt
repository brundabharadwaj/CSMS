package com.charging.system.authentication_system.service.authentication

import com.charging.system.authentication_system.entities.AuthenticationEntity
import java.util.UUID

interface AuthenticationService {
    fun findByStationIdAndDriverId(stationId:UUID,driverId:UUID): AuthenticationEntity?
}