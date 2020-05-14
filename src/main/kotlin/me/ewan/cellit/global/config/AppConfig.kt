package me.ewan.cellit.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import me.ewan.cellit.global.common.AppProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class AppConfig {
    @Bean
    fun passWordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()
}