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
        assertThat(convertedQuery.cellId).isEqualTo(null)
    }

    @Test
    fun `Convert success query to class with when parameter more than one`(){
        //given
        val query = "cellId%3D3,cellName%3DAccounting"

        //when
        val convertedQuery = ConvertQueryToClass.convert<CellQuery>(query)

        //then
        assertThat(convertedQuery.cellId).isEqualTo("3")
        assertThat(convertedQuery.cellName).isEqualTo("Accounting")
    }
}