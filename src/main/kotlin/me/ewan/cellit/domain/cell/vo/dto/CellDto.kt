package me.ewan.cellit.domain.cell.vo.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CellDto(

        var cellId: Long? = null,

        @NotEmpty
        @NotNull
        var cellName: String? = null,

        var cellDescription: String = "",

        var createDate: String = ""
)