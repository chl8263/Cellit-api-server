package me.ewan.cellit.domain.cell.model

import com.fasterxml.jackson.annotation.JsonUnwrapped
import me.ewan.cellit.domain.cell.domain.Cell
import org.springframework.hateoas.RepresentationModel

class CellModel : RepresentationModel<CellModel> {

    @JsonUnwrapped
    lateinit var cell: Cell

    constructor(cell: Cell){
        this.cell = cell
    }
}
