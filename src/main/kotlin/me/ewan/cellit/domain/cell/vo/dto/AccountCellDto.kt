package me.ewan.cellit.domain.cell.vo.dto

import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.cell.vo.domain.Cell
import me.ewan.cellit.domain.cell.vo.domain.CellRole
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class AccountCellDto(

        var accountCellId : Long? = null,

        var accountId: Long? = null,

        var cellId: Long? = null,

        var createDate: String = "",

        var cellRole : CellRole = CellRole.USER
)