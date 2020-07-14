package me.ewan.cellit.domain.cell.vo.entityModel

import com.fasterxml.jackson.annotation.JsonUnwrapped
import me.ewan.cellit.domain.cell.vo.domain.CellRequest
import me.ewan.cellit.domain.cell.vo.dto.AccountCellDto
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.domain.cell.vo.dto.CellRequestDto
import org.springframework.hateoas.RepresentationModel

class AccountCellEntityModel : RepresentationModel<AccountCellEntityModel> {

    @JsonUnwrapped
    val accountCellDto: AccountCellDto

    constructor(accountCellDto: AccountCellDto){
        this.accountCellDto = accountCellDto
    }
}
