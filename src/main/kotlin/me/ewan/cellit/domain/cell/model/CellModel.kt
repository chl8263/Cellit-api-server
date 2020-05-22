package me.ewan.cellit.domain.cell.model

import com.fasterxml.jackson.annotation.JsonUnwrapped
import me.ewan.cellit.domain.cell.api.CellController
import me.ewan.cellit.domain.cell.domain.Cell
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo

class CellModel : RepresentationModel<CellModel> {

    @JsonUnwrapped
    var cell: List<CellDto>

    constructor(cell: CellDto){
        this.cell = listOf(cell)
    }

    constructor(cell: List<CellDto>){
        this.cell = cell
    }
}
