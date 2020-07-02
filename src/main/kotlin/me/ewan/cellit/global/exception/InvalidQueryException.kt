package me.ewan.cellit.global.exception

import java.lang.RuntimeException

class InvalidQueryException(msg: String) : RuntimeException(msg) {}