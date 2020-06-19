package me.ewan.cellit.domain.account.vo.dto.validator

import me.ewan.cellit.domain.account.vo.dto.AccountDto
import me.ewan.cellit.global.ValidErrorCode.*
import org.springframework.stereotype.Component
import org.springframework.validation.Errors

@Component
class AccountDtoValidator {

    fun validate(accountDto: AccountDto, errors: Errors){

        accountDto.accountname?.let {
            if(it.contains(" ", false)) {
                errors.rejectValue("accountname", WRONG_VALUE.value, "Account name cannot have blank value")
            }
        }

        accountDto.password?.let {
            if(it.contains(" ", false)) {
                errors.rejectValue("password", WRONG_VALUE.value, "Password name cannot have blank value")
            }
        }
    }
}