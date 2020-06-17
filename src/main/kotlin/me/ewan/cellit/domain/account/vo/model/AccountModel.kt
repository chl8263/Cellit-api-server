package me.ewan.cellit.domain.account.vo.model

import com.fasterxml.jackson.annotation.JsonUnwrapped
import me.ewan.cellit.domain.account.vo.domain.Account
import org.springframework.hateoas.RepresentationModel

class AccountModel : RepresentationModel<AccountModel> {

    @JsonUnwrapped
    var accountName: String
    @JsonUnwrapped
    var role: String

    constructor(account: Account){
        this.accountName = account.accountname
        this.role = account.role.name
    }
}