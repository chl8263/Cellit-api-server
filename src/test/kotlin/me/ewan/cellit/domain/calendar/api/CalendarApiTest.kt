package me.ewan.cellit.domain.calendar.api

import me.ewan.cellit.common.BaseControllerTest
import me.ewan.cellit.domain.account.dao.AccountNotificationRepository
import me.ewan.cellit.domain.account.dao.AccountRepository
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.vo.domain.AccountNotification
import me.ewan.cellit.domain.account.vo.domain.AccountNotificationStatus
import me.ewan.cellit.domain.account.vo.domain.AccountRole
import me.ewan.cellit.domain.account.vo.dto.AccountDto
import me.ewan.cellit.domain.account.vo.dto.AccountNotificationDto
import me.ewan.cellit.domain.cell.dao.AccountCellRepository
import me.ewan.cellit.domain.cell.dao.CellRepository
import me.ewan.cellit.domain.cell.vo.domain.AccountCell
import me.ewan.cellit.domain.cell.vo.domain.Cell
import me.ewan.cellit.domain.cell.vo.domain.CellRole
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

internal class CalendarApiTest : BaseControllerTest() {

    @Test
    fun `Create Calendar`(){
        //given
    }

    @Test
    fun `Get Calendar data based on month`(){
        //given
    }

}