package me.ewan.cellit.domain.account.vo.model

import com.fasterxml.jackson.annotation.JsonUnwrapped
import org.springframework.hateoas.RepresentationModel

class AccountModel : RepresentationModel<AccountModel>() {

    @JsonUnwrapped
    var cell: String = ""

}