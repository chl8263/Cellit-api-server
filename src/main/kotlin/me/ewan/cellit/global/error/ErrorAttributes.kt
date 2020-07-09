package me.ewan.cellit.global.error

import me.ewan.cellit.global.error.vo.ErrorVo
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.context.request.WebRequest

@Component
class ErrorAttributes : DefaultErrorAttributes() {

    override fun getErrorAttributes(webRequest: WebRequest?, includeStackTrace: Boolean): MutableMap<String, Any> {
        val containAttributes = HashMap<String, Any>()
        val errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace)
        containAttributes.set("errors", errorAttributes)
        return containAttributes
    }

    fun getErrorAttributes2(errorList: List<ErrorVo>): HashMap<String, List<ErrorVo>> {
        val containAttributes = HashMap<String, List<ErrorVo>>()
        //val containAttributes = HashMap<String, List<Map<String,ErrorVo>>>()
//        errorList.map {
//            val errorAttributes = HashMap<String, ErrorVo>()
//        }
        containAttributes.set("errors", errorList)
        return containAttributes
    }
}