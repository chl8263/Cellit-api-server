package me.ewan.cellit.global.common

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.ewan.cellit.global.exception.InvalidQueryException
import me.ewan.cellit.global.security.dtos.JwtAuthenticationDto
import org.codehaus.jackson.map.ObjectMapper
import org.springframework.stereotype.Component
import java.lang.Exception
import java.lang.StringBuilder

@Component
class ConvertQueryToClass {

    companion object{
        const val INVALID_EQUAL_MSG = "This query hasn't valid Equal at API server."
        const val EQUAL = "="

        inline fun <reified T> convert(query: String): T{
            try{
                val json = convertToJson(query)
                val formLoginDto = ObjectMapper().readValue(json, T::class.java)
                return formLoginDto
            }catch (e: Exception){
                throw e
            }
        }

        fun convertToJson(query: String): String{
            val sb = StringBuilder()
            sb.append("{")

            val split = query.split(",")
            split.forEachIndexed { i, it ->
                if(!it.contains(EQUAL)) throw InvalidQueryException(INVALID_EQUAL_MSG)
                val colonSplit = it.split("=")
                sb.append("\"${colonSplit[0]}\"")
                sb.append(":")
                sb.append("\"${colonSplit[1]}\"")
                if(i != split.size-1){
                    sb.append(",")
                }
            }
            sb.append("}")
            return sb.toString()
        }
    }
}