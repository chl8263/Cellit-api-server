package me.ewan.cellit.domain.cell.vo.query

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CellQuery(
        var cellId: Long? = null,

        var cellName: String? = null
)