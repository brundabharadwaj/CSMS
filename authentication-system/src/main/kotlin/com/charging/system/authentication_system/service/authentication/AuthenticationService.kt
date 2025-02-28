package com.charging.system.authentication_system.service.authentication

import java.util.UUID

interface AuthenticationService {
    fun findByStationIdAndDriverId(stationId:UUID,driverId:UUID)
}