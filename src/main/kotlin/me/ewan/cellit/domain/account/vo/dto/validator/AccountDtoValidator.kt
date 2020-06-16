package me.ewan.cellit.domain.account.vo.dto.validator

import me.ewan.cellit.domain.account.vo.dto.AccountDto
import org.springframework.stereotype.Component
import org.springframework.validation.Errors

@Component
class AccountDtoValidator {

    fun validate(accountDto: AccountDto, errors: Errors){
        if(accountDto.accountname?.isBlank()!!){
            errors.rejectValue("accountname", "wrongValue", "accountname is wrong")
        }

        if(accountDto.password?.isBlank()!!){
            errors.rejectValue("password", "wrongValue", "password is wrong")
        }
    }
}