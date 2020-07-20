package me.ewan.cellit.global.common

abstract class Query(
        open var offset: Long? = null,
        open var limit: Long? = null
)