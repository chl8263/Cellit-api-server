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

        inline fun <reified T> convert(query: String? = null, offset: Int? = null, limit: Int? = null): T{
            try{
                val json = convertToJson(query, offset, limit)
                println(json)
                val convertedClass = ObjectMapper().readValue(json, T::class.java)
                return convertedClass
            }catch (e: Exception){
                throw e
            }
        }

        fun convertToJson(query: String?, offset: Int?, limit: Int?): String{
            val sb = StringBuilder()
            sb.append("{")

            query?.let {
                val split = query.split(",")
                split.forEachIndexed { i, it ->
                    if (!it.contains(EQUAL)) throw InvalidQueryException(INVALID_EQUAL_MSG)
                    val colonSplit = it.split("=")
                    sb.append("\"${colonSplit[0]}\"")
                    sb.append(":")
                    sb.append("\"${colonSplit[1]}\"")
                    if (i != split.size - 1) {
                        sb.append(",")
                    }
                }
            }
            offset?.let {
                query?.let { sb.append(",") }
                sb.append("\"offset\"")
                sb.append(":")
                sb.append("\"$offset\"")
            }
            limit?.let {
                offset?.let { sb.append(",") }
                sb.append("\"limit\"")
                sb.append(":")
                sb.append("\"$limit\"")
            }
            sb.append("}")
            return sb.toString()
        }
    }
}