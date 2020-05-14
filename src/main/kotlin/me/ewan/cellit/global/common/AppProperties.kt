package me.ewan.cellit.global.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import javax.validation.constraints.NotEmpty

@Component
@ConfigurationProperties(prefix = "my-app")
class AppProperties{

        constructor()

        lateinit var adminUsername: String
        lateinit var adminPassword: String
        lateinit var userUsername: String
        lateinit var userPassword: String
        lateinit var testAdminUsername: String
        lateinit var testAdminPassword: String
        lateinit var testUserUsername: String
        lateinit var testUserPassword: String
        lateinit var clientId: String
        lateinit var clientSecret: String
}