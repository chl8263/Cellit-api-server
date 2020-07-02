package me.ewan.cellit.global.common

import me.ewan.cellit.domain.cell.vo.query.CellQuery
import me.ewan.cellit.global.exception.InvalidQueryException
import org.assertj.core.api.Assertions.assertThat
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

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
        assertThat(convertedQuery.cellId).isEqualTo(3L)
        assertThat(convertedQuery.cellName).isEqualTo("Accounting")
    }

    @Test
    fun `Fail convert query with invalid colon`(){
        //given
        val query = "cellId%5D3"

        //when
        val convertQueryToClass =assertThrows(InvalidQueryException::class.java){ConvertQueryToClass.convert<CellQuery>(query)}
        val errorMsg = convertQueryToClass.message

        //then
        assertThat(errorMsg).isEqualTo(ConvertQueryToClass.INVALID_COLON_MSG)
    }

    @Test
    fun `Fail convert query with invalid query class member`(){
        //given
        val query = "cellIDDD%3D3"

        //when
        val convertQueryToClass = assertThrows(UnrecognizedPropertyException::class.java){ConvertQueryToClass.convert<CellQuery>(query)}
        val errorMsg = convertQueryToClass.message

        //then
        assertThat(errorMsg).isNotBlank()
    }
}