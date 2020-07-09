package me.ewan.cellit.domain.account.vo.dto.validator

import me.ewan.cellit.domain.account.vo.dto.AccountDto
import me.ewan.cellit.global.ValidErrorCode.*
import me.ewan.cellit.global.error.vo.ErrorVo
import org.springframework.stereotype.Component
import org.springframework.validation.Errors

@Component
class AccountDtoValidator {

    fun validate(accountDto: AccountDto): List<ErrorVo>{

        val errorList = ArrayList<ErrorVo>()

        accountDto.accountname?.let {
            if(it.contains(" ", false)) {
                errorList.add(ErrorVo(status = 400, message = "Account name cannot have blank value"))
                //errors.rejectValue("accountname", WRONG_VALUE.value, "Account name cannot have blank value")
            }
        }

        accountDto.password?.let {
            if(it.contains(" ", false)) {
                errorList.add(ErrorVo(status = 400, message = "Account name cannot have blank value"))
                //errors.rejectValue("password", WRONG_VALUE.value, "Password name cannot have blank value")
            }
        }

        return errorList
    }
}