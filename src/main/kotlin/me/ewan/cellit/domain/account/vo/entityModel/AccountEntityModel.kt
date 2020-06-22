package me.ewan.cellit.domain.account.vo.entityModel

import com.fasterxml.jackson.annotation.JsonUnwrapped
import me.ewan.cellit.domain.account.vo.domain.Account
import org.springframework.hateoas.RepresentationModel

class AccountEntityModel : RepresentationModel<AccountEntityModel> {

    @JsonUnwrapped
    var accountId: Long
    @JsonUnwrapped
    var accountName: String
    @JsonUnwrapped
    var role: String

    constructor(account: Account){
        this.accountId = account.accountId!!
        this.accountName = account.accountname
        this.role = account.role.name
    }
}