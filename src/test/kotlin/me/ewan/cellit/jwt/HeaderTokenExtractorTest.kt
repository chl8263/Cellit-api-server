package me.ewan.cellit.jwt

import me.ewan.cellit.global.security.HeaderTokenExtractor
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat


internal class HeaderTokenExtractorTest{

    private val tokenExtractor = HeaderTokenExtractor()

    @Test
    fun `test jwt extract`(){

        //given
        val header = "Bearer ashgasvmlksdmvkm.asdasfqwc123.qwcqwcqw"

        //then
        assertThat(tokenExtractor.extract(header)).isEqualTo("ashgasvmlksdmvkm.asdasfqwc123.qwcqwcqw")
    }

}