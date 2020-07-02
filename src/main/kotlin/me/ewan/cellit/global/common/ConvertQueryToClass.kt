package me.ewan.cellit.global.common

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.ewan.cellit.global.security.dtos.JwtAuthenticationDto
import org.codehaus.jackson.map.ObjectMapper
import java.lang.Exception
import java.lang.StringBuilder

class ConvertQueryToClass {

    companion object{
        const val COLON = "%3D"
        inline fun <reified T> convert(query: String): T{
            try{
                val json = convertToJson(query)
                println("???????")
                println(json)
                val formLoginDto = ObjectMapper().readValue(json, T::class.java)
                return formLoginDto
            }catch (e: Exception){
                throw e
            }
        }

        fun convertToJson(query: String): String{
            //val jsonObject = JsonParser().parse(query).asJsonObject
            //query=cellName%3D,
            val sb = StringBuilder()
            sb.append("{")

            val split = query.split(",")
            println("!!!!!!!!!!!!!!")
            split.forEach {
                if(!it.contains(COLON)) throw Exception()
                val colonSplit = it.split(COLON)
                sb.append("\"${colonSplit[0]}\"")
                sb.append(":")
                sb.append("\"${colonSplit[1]}\"")
            }
            sb.append("}")
            return sb.toString()
        }
    }
}