package me.ewan.cellit.global.common

abstract class Query(
        open var offset: Int? = null,
        open var limit: Int? = null
)