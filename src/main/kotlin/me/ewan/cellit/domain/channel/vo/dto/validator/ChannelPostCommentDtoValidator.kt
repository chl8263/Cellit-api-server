package me.ewan.cellit.domain.cell.vo.dto.validator

import me.ewan.cellit.domain.account.vo.dto.AccountDto
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.domain.channel.vo.dto.ChannelPostCommentDto
import me.ewan.cellit.domain.channel.vo.dto.ChannelPostDto
import me.ewan.cellit.global.ValidErrorCode.*
import me.ewan.cellit.global.error.vo.ErrorVo
import me.ewan.cellit.global.error.vo.HTTP_STATUS
import me.ewan.cellit.global.error.vo.HTTP_STATUS.BAD_REQUEST
import org.springframework.stereotype.Component
import org.springframework.validation.Errors

@Component
class ChannelPostCommentDtoValidator {

    fun validate(channelPostCommentDto: ChannelPostCommentDto): ArrayList<ErrorVo>{

        val errorList = ArrayList<ErrorVo>()

        channelPostCommentDto.channelPostComment?.let {
            if(it.isNullOrBlank()) {
                errorList.add(ErrorVo(status = BAD_REQUEST, message = "Channel post comment cannot become blank."))
            }
        }

        if(channelPostCommentDto.accountId == null){
            errorList.add(ErrorVo(status = BAD_REQUEST, message = "Account Id has not be null."))
        }

        return errorList
    }
}