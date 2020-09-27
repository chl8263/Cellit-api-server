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

package me.ewan.cellit.domain.account.vo.dto.validator

import me.ewan.cellit.domain.account.vo.dto.AccountDto
import me.ewan.cellit.global.error.vo.ErrorVo
import me.ewan.cellit.global.error.vo.HTTP_STATUS.BAD_REQUEST
import org.springframework.stereotype.Component

/**
 * <code>Validator</code> for <code>AccountDto</code>.
 *
 * @author Ewan
 */
@Component
class AccountDtoValidator {

    fun validate(accountDto: AccountDto): List<ErrorVo>{

        val errorList = ArrayList<ErrorVo>()

        accountDto.accountname?.let {
            if(it.contains(" ", false)) {
                errorList.add(ErrorVo(status = BAD_REQUEST, message = "Account name cannot have blank value"))
            }
        }

        accountDto.password?.let {
            if(it.contains(" ", false)) {
                errorList.add(ErrorVo(status = BAD_REQUEST, message = "Account name cannot have blank value"))
            }
        }

        return errorList
    }
}