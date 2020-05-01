package me.ewan.cellit.account

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Account(
        @Id @GeneratedValue
        var id: Long? = null,

        @Column(unique = true)
        var username: String,

        var password: String,

        var role: String? = null
)