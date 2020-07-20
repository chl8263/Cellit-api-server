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
        /*
        *  When request url, must use 3%D between variable and value.
        * */
        val query = "cellName=Accounting"

        //when
        val convertedQuery = ConvertQueryToClass.convert<CellQuery>(query)

        //then
        assertThat(convertedQuery.cellName).isEqualTo("Accounting", 1, 1)
        assertThat(convertedQuery.cellId).isEqualTo(null)
    }

    @Test
    fun `Convert success query to class with when parameter more than one`(){
        //given
        /*
        *  When request url, must use 3%D between variable and value.
        * */
        val query = "cellId=3,cellName=Accounting"

        //when
        val convertedQuery = ConvertQueryToClass.convert<CellQuery>(query, 1, 1)

        //then
        assertThat(convertedQuery.cellId).isEqualTo(3L)
        assertThat(convertedQuery.cellName).isEqualTo("Accounting")
    }

    @Test
    fun `Fail convert query with invalid colon`(){
        //given
        /*
        *  When request url, must use 3%D between variable and value.
        * */
        val query = "cellId-3"

        //when
        val convertQueryToClass =assertThrows(InvalidQueryException::class.java){ConvertQueryToClass.convert<CellQuery>(query)}
        val errorMsg = convertQueryToClass.message

        //then
        assertThat(errorMsg).isEqualTo(ConvertQueryToClass.INVALID_EQUAL_MSG)
    }

    @Test
    fun `Fail convert query with invalid query class member`(){
        //given
        /*
        *  When request url, must use 3%D between variable and value.
        * */
        val query = "cellIDD=3D3"

        //when
        val convertQueryToClass = assertThrows(UnrecognizedPropertyException::class.java){ConvertQueryToClass.convert<CellQuery>(query)}
        val errorMsg = convertQueryToClass.message

        //then
        assertThat(errorMsg).isNotBlank()
    }
}