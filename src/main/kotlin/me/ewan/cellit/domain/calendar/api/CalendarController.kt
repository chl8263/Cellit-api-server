package me.ewan.cellit.domain.calendar.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/calendars"], produces = [MediaTypes.HAL_JSON_VALUE])
class CalendarController {

    @GetMapping("get")
    fun getCalendarDataWithMonth(){

    }
}