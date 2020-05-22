package me.ewan.cellit.global.config

import me.ewan.cellit.global.interceptor.CertificationInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
class WebConfig : WebMvcConfigurer{

    @Autowired
    lateinit var certificationInterceptor: CertificationInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(certificationInterceptor)
                .excludePathPatterns("/signUp", "/login**", "/loginError", "/logout")
    }
}