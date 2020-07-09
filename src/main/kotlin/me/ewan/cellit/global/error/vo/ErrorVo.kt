package me.ewan.cellit.global.error.vo

import org.hibernate.annotations.CreationTimestamp
import java.text.SimpleDateFormat
import java.util.*

data class ErrorVo(
        var status: Int,
        var message: String,
        var timestamp: String = SimpleDateFormat("yyyy-MM-dd.HH:mm").format(Date())
)
