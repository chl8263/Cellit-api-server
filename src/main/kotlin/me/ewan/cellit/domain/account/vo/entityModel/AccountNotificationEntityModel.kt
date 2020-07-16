package me.ewan.cellit.domain.account.vo.entityModel

import com.fasterxml.jackson.annotation.JsonUnwrapped
import me.ewan.cellit.domain.account.vo.domain.Account
import org.springframework.hateoas.RepresentationModel

class AccountNotificationEntityModel : RepresentationModel<AccountNotificationEntityModel> {

    @JsonUnwrapped
    val accountId: Long
    @JsonUnwrapped
    val message: String

    constructor(accountId: Long, message: String){
        this.accountId = accountId
        this.message = message
    }
}