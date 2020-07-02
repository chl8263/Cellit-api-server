package me.ewan.cellit.domain.cell.vo.entityModel

import com.fasterxml.jackson.annotation.JsonUnwrapped
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import org.springframework.hateoas.RepresentationModel

class CellEntityModel : RepresentationModel<CellEntityModel> {

    @JsonUnwrapped
    var cell: CellDto

    var role: String

    constructor(cell: CellDto, role: String){
        this.cell = cell
        this.role = role
    }

}
