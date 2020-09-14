/********************************************************************************************
 * Copyright (c) 2020 Ewan Choi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************************/

package me.ewan.cellit.global.common

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.ewan.cellit.global.exception.InvalidQueryException
import me.ewan.cellit.global.security.dtos.JwtAuthenticationDto
import org.codehaus.jackson.map.ObjectMapper
import org.springframework.stereotype.Component
import java.lang.Exception
import java.lang.StringBuilder

/**
 * Util class for Convert Query to Class.
 *
 * @author Ewan
 */
@Component
class ConvertQueryToClass {

    companion object{
        const val INVALID_EQUAL_MSG = "This query hasn't valid Equal at API server."
        const val EQUAL = "="

        /**
         * Convert query to Generic Class.
         *
         * @author Ewan
         * @param query query for retrieve
         * @param offset start number of list
         * @param limit end number of list
         * @return Generic class
         */
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

        /**
         * Conver Json to Class.
         *
         * @author Ewan
         * @param query query for retrieve
         * @param offset start number of list
         * @param limit end number of list
         * @return Generic class
         */
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