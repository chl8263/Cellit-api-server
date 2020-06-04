package me.ewan.cellit.domain.account.vo.dto

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import me.ewan.cellit.domain.account.vo.domain.AccountRole
import me.ewan.cellit.domain.cell.vo.domain.AccountCell
import org.codehaus.jackson.annotate.JsonIgnore
import javax.persistence.*

data class AccountDto (
        var accountname: String? = null,
        var password: String? = null
        )