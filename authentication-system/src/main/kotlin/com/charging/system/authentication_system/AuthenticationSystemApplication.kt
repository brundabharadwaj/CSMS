package com.charging.system.authentication_system

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableAsync


@EnableJpaRepositories
@SpringBootApplication
class AuthenticationSystemApplication

fun main(args: Array<String>) {
	runApplication<AuthenticationSystemApplication>(*args)
}
