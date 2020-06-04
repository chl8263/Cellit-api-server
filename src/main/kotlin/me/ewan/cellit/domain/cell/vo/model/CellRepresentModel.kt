package me.ewan.cellit.domain.cell.vo.model

import com.fasterxml.jackson.annotation.JsonUnwrapped
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import org.springframework.hateoas.RepresentationModel

class CellRepresentModel : RepresentationModel<CellRepresentModel> {

    @JsonUnwrapped
    var cell: CellDto

    constructor(cell: CellDto){
        this.cell = cell
    }

}
