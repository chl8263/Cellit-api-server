package me.ewan.cellit.domain.cell.vo.dto.validator

import me.ewan.cellit.domain.account.vo.dto.AccountDto
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.global.ValidErrorCode.*
import org.springframework.stereotype.Component
import org.springframework.validation.Errors

@Component
class CellDtoValidator {

    fun validate(cellDto: CellDto, errors: Errors){

        cellDto.cellName?.let {
            if(it.isNullOrBlank()) {
                errors.rejectValue("cell name", WRONG_VALUE.value, "cell name cannot become blank")
            }
        }
    }
}