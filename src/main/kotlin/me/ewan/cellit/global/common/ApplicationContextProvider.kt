package me.ewan.cellit.global.common

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component


@Component
class ApplicationContextProvider : ApplicationContextAware{

    private var context: ApplicationContext? = null

    fun getApplicationContext(): ApplicationContext? {
        return context
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

}