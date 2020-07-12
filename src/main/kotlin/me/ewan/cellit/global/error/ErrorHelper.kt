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