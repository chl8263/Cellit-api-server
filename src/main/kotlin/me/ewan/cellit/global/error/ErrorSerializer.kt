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

package me.ewan.cellit.global.error

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.validation.Errors
import java.io.IOException

/**
 * For use this .
 *
 * @author Ewan
 */
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

        gen.writeEndArray()
    }
}