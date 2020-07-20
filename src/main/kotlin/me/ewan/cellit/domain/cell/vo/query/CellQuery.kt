package me.ewan.cellit.domain.cell.vo.query

import me.ewan.cellit.global.common.Query
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CellQuery(
        var cellId: Long? = null,

        var cellName: String? = null,

        override var offset: Int? = null,

        override var limit: Int? = null

) : Query(offset, limit)