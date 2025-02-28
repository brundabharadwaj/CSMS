package com.charging.system.authentication_system.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor
import java.util.UUID

@Entity(name="authentication")
@Builder
@NoArgsConstructor
@AllArgsConstructor
 class AuthenticationEntity(
    @Id val stationUuid: UUID = UUID.randomUUID(), // Provide a default value
    val driverId: UUID = UUID.randomUUID()
)
