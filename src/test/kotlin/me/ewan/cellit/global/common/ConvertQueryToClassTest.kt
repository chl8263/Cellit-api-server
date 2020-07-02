package me.ewan.cellit.global.common

import me.ewan.cellit.domain.cell.vo.query.CellQuery
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ConvertQueryToClassTest{

    @Test
    fun `Convert success query to class`(){
        //given
        val query = "cellName%3DAccounting"

        //when
        val convertedQuery = ConvertQueryToClass.convert<CellQuery>(query)

        //then
        assertThat(convertedQuery.cellName).isEqualTo("Accounting")
    }
}