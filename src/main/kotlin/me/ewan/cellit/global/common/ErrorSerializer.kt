package me.ewan.cellit.global.common

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import org.springframework.validation.Errors
import java.io.IOException

class ErrorSerializer : JsonSerializer<Errors>(){

    override fun serialize(value: Errors, gen: JsonGenerator, serializers: SerializerProvider) {

        gen.writeStartArray()

        value.fieldErrors.forEach {
            try{
                gen.writeStartObject()
                gen.writeStringField("field", it.field)
                gen.writeStringField("objectName", it.objectName)
                gen.writeStringField("code", it.code)
                gen.writeStringField("defaultMessage", it.defaultMessage)
                it.rejectedValue?.let { x ->  gen.writeStringField("rejectedValue", it.rejectedValue.toString()) }
                gen.writeEndObject()
            }catch (e: IOException){
                e.printStackTrace()
            }
        }

//        value.globalErrors.forEach {
//            try{
//                gen.writeStartObject()
//                gen.writeStringField("objectName", it.objectName)
//                gen.writeStringField("code", it.code)
//                gen.writeStringField("defaultMessage", it.defaultMessage)
//                gen.writeEndObject()
//            }catch (e: IOException){
//                e.printStackTrace()
//            }
//        }

        gen.writeEndArray()
    }
}