package me.ewan.cellit.global.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.Errors

@Component
class ErrorToJson {

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    fun convert(errors: Errors): String{
        val simpleModule = SimpleModule()
        simpleModule.addSerializer(Errors::class.java, ErrorSerializer())
        objectMapper.registerModule(simpleModule)
        return objectMapper.writeValueAsString(errors)
    }

}