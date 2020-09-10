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

import me.ewan.cellit.global.error.vo.ErrorVo
import me.ewan.cellit.global.error.vo.HTTP_STATUS
import me.ewan.cellit.global.error.vo.HTTP_STATUS.BAD_REQUEST
import org.springframework.stereotype.Component

@Component
class ErrorHelper{

    fun getErrorAttributes(errorList: List<ErrorVo>): HashMap<String, List<ErrorVo>> {
        val containAttributes = HashMap<String, List<ErrorVo>>()
        containAttributes["errors"] = errorList
        return containAttributes
    }

    fun addErrorAttributes(status: Int, message: String, errorList: ArrayList<ErrorVo>): ArrayList<ErrorVo>{
        return if(errorList.isEmpty()){
            errorList.add(ErrorVo(status = status, message = message))
            errorList
        }else {
            arrayListOf(ErrorVo(status = status, message = message))
        }
    }

    fun getUnexpectError(message: String): HashMap<String, List<ErrorVo>> {
        val containAttributes = HashMap<String, List<ErrorVo>>()
        containAttributes["errors"] = arrayListOf(ErrorVo(status = BAD_REQUEST, message = message))
        return containAttributes
    }
}