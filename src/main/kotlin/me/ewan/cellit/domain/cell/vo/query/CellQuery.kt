package me.ewan.cellit.domain.cell.vo.query

import me.ewan.cellit.global.common.Query
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CellQuery(
        var cellId: Long? = null,

        var cellName: String? = null,

        override var offset: Long? = null,

        override var limit: Long? = null

) : Query(offset, limit)