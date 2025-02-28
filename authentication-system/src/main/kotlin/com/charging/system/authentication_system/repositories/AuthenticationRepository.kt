package com.charging.system.authentication_system.repositories


import com.charging.system.authentication_system.entities.AuthenticationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AuthenticationRepository : JpaRepository<AuthenticationEntity, UUID> {
    fun findByStationUuidAndDriverId(stationUuid: UUID, driverId: UUID): AuthenticationEntity?
}
