package me.ewan.cellit.domain.account.vo.dto.validator

import me.ewan.cellit.domain.account.vo.dto.AccountDto
import me.ewan.cellit.global.ValidErrorCode.*
import me.ewan.cellit.global.error.vo.ErrorVo
import me.ewan.cellit.global.error.vo.HTTP_STATUS.BAD_REQUEST
import org.springframework.stereotype.Component
import org.springframework.validation.Errors

@Component
class AccountDtoValidator {

    fun validate(accountDto: AccountDto): List<ErrorVo>{

        val errorList = ArrayList<ErrorVo>()

        accountDto.accountname?.let {
            if(it.contains(" ", false)) {
                errorList.add(ErrorVo(status = BAD_REQUEST, message = "Account name cannot have blank value"))
            }
        }

        accountDto.password?.let {
            if(it.contains(" ", false)) {
                errorList.add(ErrorVo(status = BAD_REQUEST, message = "Account name cannot have blank value"))
            }
        }

        return errorList
    }
}