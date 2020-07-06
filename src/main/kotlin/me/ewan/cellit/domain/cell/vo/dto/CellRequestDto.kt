package me.ewan.cellit.domain.cell.vo.dto

import me.ewan.cellit.domain.cell.vo.domain.Cell
import java.text.SimpleDateFormat
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CellRequestDto(

        var cellRequestId: Long? = null,

        var cellId: Long? = null,

        var accountId: Long? = null,

        var createDate: String? = null
)