/********************************************************************************************
 * Copyright (c) 2020 Ewan Choi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************************/

package me.ewan.cellit.domain.cell.vo.dto.validator

import me.ewan.cellit.domain.account.vo.dto.AccountDto
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.domain.channel.vo.dto.ChannelPostDto
import me.ewan.cellit.global.ValidErrorCode.*
import me.ewan.cellit.global.error.vo.ErrorVo
import me.ewan.cellit.global.error.vo.HTTP_STATUS
import me.ewan.cellit.global.error.vo.HTTP_STATUS.BAD_REQUEST
import org.springframework.stereotype.Component
import org.springframework.validation.Errors

@Component
class ChannelPostDtoValidator {

    fun validate(channelPostDto: ChannelPostDto): ArrayList<ErrorVo>{

        val errorList = ArrayList<ErrorVo>()

        channelPostDto.channelPostName?.let {
            if(it.isNullOrBlank()) {
                errorList.add(ErrorVo(status = BAD_REQUEST, message = "Channel post name cannot become blank"))
            }
        }

        channelPostDto.channelPostContent?.let {
            if(it.isNullOrBlank()) {
                errorList.add(ErrorVo(status = BAD_REQUEST, message = "Channel post content cannot become blank"))
            }
        }
        return errorList
    }
}