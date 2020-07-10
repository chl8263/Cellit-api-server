package me.ewan.cellit.domain.cell.vo.dto.validator

import me.ewan.cellit.domain.account.vo.dto.AccountDto
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.global.ValidErrorCode.*
import me.ewan.cellit.global.error.vo.ErrorVo
import me.ewan.cellit.global.error.vo.HTTP_STATUS
import me.ewan.cellit.global.error.vo.HTTP_STATUS.BAD_REQUEST
import org.springframework.stereotype.Component
import org.springframework.validation.Errors

@Component
class CellDtoValidator {

    fun validate(cellDto: CellDto): List<ErrorVo>{

        val errorList = ArrayList<ErrorVo>()

        cellDto.cellName?.let {
            if(it.isNullOrBlank()) {
                errorList.add(ErrorVo(status = BAD_REQUEST, message = "cell name cannot become blank"))
            }
        }
        return errorList
    }
}