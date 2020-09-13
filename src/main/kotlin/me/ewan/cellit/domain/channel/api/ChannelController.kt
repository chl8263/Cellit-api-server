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

package me.ewan.cellit.domain.channel.api

import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.cell.api.CellController
import me.ewan.cellit.domain.cell.vo.dto.CellRequestDto
import me.ewan.cellit.domain.cell.vo.dto.validator.ChannelPostCommentDtoValidator
import me.ewan.cellit.domain.cell.vo.dto.validator.ChannelPostDtoValidator
import me.ewan.cellit.domain.cell.vo.entityModel.CellRequestEntityModel
import me.ewan.cellit.domain.channel.service.ChannelService
import me.ewan.cellit.domain.channel.vo.domain.ChannelPost
import me.ewan.cellit.domain.channel.vo.domain.ChannelPostComment
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import me.ewan.cellit.domain.channel.vo.dto.ChannelPostCommentDto
import me.ewan.cellit.domain.channel.vo.dto.ChannelPostContentDto
import me.ewan.cellit.domain.channel.vo.dto.ChannelPostDto
import me.ewan.cellit.domain.channel.vo.entityModel.ChannelEntityModel
import me.ewan.cellit.domain.channel.vo.entityModel.ChannelPostCommentEntityModel
import me.ewan.cellit.domain.channel.vo.entityModel.ChannelPostContentEntityModel
import me.ewan.cellit.domain.channel.vo.entityModel.ChannelPostEntityModel
import me.ewan.cellit.global.error.Const
import me.ewan.cellit.global.error.Const.UNEXPECTED_ERROR_WORD
import me.ewan.cellit.global.error.ErrorHelper
import me.ewan.cellit.global.error.vo.ErrorVo
import me.ewan.cellit.global.error.vo.HTTP_STATUS.BAD_REQUEST
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Ewan
 */
@RestController
@RequestMapping(value = ["/api/channels"], produces = [MediaTypes.HAL_JSON_VALUE])
class ChannelController {

    private val log = KotlinLogging.logger {}

    @Autowired
    lateinit var channelService: ChannelService

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var modelMapper: ModelMapper

    @Autowired
    private lateinit var errorHelper: ErrorHelper

    @Autowired
    private lateinit var channelPostDtoValidator: ChannelPostDtoValidator

    @Autowired
    private lateinit var channelPostCommentDtoValidator: ChannelPostCommentDtoValidator

    /**
     * Create new Channel.
     *
     * @author Ewan
     * @param channelDto channel information
     * @return a ResponseEntity as satisfied with REST hateoas
     */
    @PostMapping
    fun createChannel(@RequestBody channelDto: ChannelDto): ResponseEntity<Any> {
        try {
            val savedChannel = channelService.createChannel(channelDto)

            val entityModel = savedChannel.run {
                val tempChannelDto = modelMapper.map(this, ChannelDto::class.java)
                val channelEntityModel = ChannelEntityModel(tempChannelDto)
                val selfLink = linkTo(ChannelController::class.java).slash(this.channelId).withSelfRel()
                channelEntityModel.add(selfLink)
            }

            val linkBuilder = linkTo(ChannelController::class.java)
            val createUrl = linkBuilder.toUri()

            return ResponseEntity.created(createUrl).body(entityModel)

        } catch (e: Exception) {
            val body = errorHelper.getUnexpectError(UNEXPECTED_ERROR_WORD)
            return ResponseEntity.badRequest().body(body)
        }
    }

    /**
     * Update Channel Active status.
     *
     * @author Ewan
     * @param channelId
     * @param activeState
     * @return a ResponseEntity as satisfied with REST hateoas
     */
    @PatchMapping("{channelId}/active")
    fun updateChannelActive(@PathVariable channelId: Long,
                            @RequestBody activeState: Int): ResponseEntity<Any>{
        try {
            // s: validations
            val errorList = ArrayList<ErrorVo>()

            val foundedChannel = channelService.getChannelByChannelId(channelId)
            if (foundedChannel == null) {
                errorHelper.addErrorAttributes(BAD_REQUEST, "Not exits this Channel.", errorList)
            }

            if (activeState !is Int) {
                errorHelper.addErrorAttributes(BAD_REQUEST, "Active state must be integer", errorList)
            }

            if (errorList.isNotEmpty()) {
                val body = errorHelper.getErrorAttributes(errorList)
                return ResponseEntity.badRequest().body(body)
            }
            // e: validations

            foundedChannel!!.active = activeState
            val savedChannel = channelService.saveChannel(foundedChannel!!)

            val entityModel = savedChannel.run {
                val tempChannelDto = modelMapper.map(this, ChannelDto::class.java)
                val channelEntityModel = ChannelEntityModel(tempChannelDto)
                val selfLink = linkTo(ChannelController::class.java).slash(channelId).withSelfRel()
                channelEntityModel.add(selfLink)
            }

            return ResponseEntity.ok(entityModel)

        } catch (e: Exception) {
            log.error { e.message }
            val body = errorHelper.getUnexpectError(UNEXPECTED_ERROR_WORD)
            return ResponseEntity.badRequest().body(body)
        }
    }

    /**
     * Create new ChannelPost.
     *
     * @author Ewan
     * @param channelId
     * @param channelPostDto
     * @return a ResponseEntity as satisfied with REST hateoas
     */
    @PostMapping("{channelId}/channelPosts")
    fun createChannelPosts(@PathVariable channelId: Long,
                           @RequestBody channelPostDto: ChannelPostDto): ResponseEntity<Any> {
        try {
            // s: validations
            val errorList = channelPostDtoValidator.validate(channelPostDto)

            val foundedChannel = channelService.getChannelByChannelId(channelId)
            if (foundedChannel == null) {
                errorHelper.addErrorAttributes(BAD_REQUEST, "Not exits this Channel.", errorList)
            }

            if (errorList.isNotEmpty()) {
                val body = errorHelper.getErrorAttributes(errorList)
                return ResponseEntity.badRequest().body(body)
            }
            // e: validations

            val channelPost = ChannelPost(channelPostName = channelPostDto.channelPostName!!,
                    //channelPostContent = channelPostDto.channelPostContent!!,
                    accountId = channelPostDto.accountId!!,
                    accountName = channelPostDto.accountName!!,
                    channel = foundedChannel!!
            )

            val savedChannelPost = channelService.saveChannelPost(channelPost, channelPostDto.channelPostContent!!)

            val entityModel = savedChannelPost.run {
                //val tempChannelPostDto = modelMapper.map(this, ChannelPostDto::class.java)
                val channelPostEntityModel = ChannelPostEntityModel(this)
                val selfLink = linkTo(ChannelController::class.java).slash(channelId).slash("/channelPost").slash(this.channelPostId).withSelfRel()
                channelPostEntityModel.add(selfLink)
            }

            val linkBuilder = linkTo(methodOn(ChannelController::class.java).createChannelPosts(channelId, channelPostDto))
            val createUrl = linkBuilder.toUri()

            return ResponseEntity.created(createUrl).body(entityModel)

        } catch (e: Exception) {
            log.error { e.message }
            val body = errorHelper.getUnexpectError(UNEXPECTED_ERROR_WORD)
            return ResponseEntity.badRequest().body(body)
        }
    }

    /**
     * Retrieve ChannelPost List as Pagenation.
     *
     * @author Ewan
     * @param channelId
     * @param assembler
     * @param pageable
     * @param postNameToSearch
     * @return a ResponseEntity as satisfied with REST hateoas
     */
    @GetMapping("{channelId}/channelPosts")
    fun getChannelPost(@PathVariable channelId: Long,
                       postNameToSearch: String,
                       pageable: Pageable,
                       assembler: PagedResourcesAssembler<ChannelPost>): ResponseEntity<Any> {
        try {
            // s: validations
            val foundChannel = channelService.getChannelByChannelId(channelId)
            if (foundChannel == null) {
                val body = errorHelper.getUnexpectError("Not exits this Channel.")
                return ResponseEntity.badRequest().body(body)
            }
            // e: validations

            val page = this.channelService.getChannelPosts(channelId, postNameToSearch, pageable)
            val pageEntityModel = assembler.toModel(page) { entity: ChannelPost ->
                val channelPostEntityModel = ChannelPostEntityModel(entity)
                val selfLink = linkTo(ChannelController::class.java).slash(channelId).slash("/channelPosts").slash(entity.channelPostId).withSelfRel()
                channelPostEntityModel.add(selfLink)
            }

            return ResponseEntity.ok(pageEntityModel)

        } catch (e: Exception) {
            val body = errorHelper.getUnexpectError(UNEXPECTED_ERROR_WORD)
            return ResponseEntity.badRequest().body(body)
        }
    }

    /**
     * Retrieve ChannelPost List as Pagenation.
     *
     * @author Ewan
     * @param channelId
     * @param assembler
     * @param pageable
     * @param postNameToSearch
     * @return a ResponseEntity as satisfied with REST hateoas
     */
    @GetMapping("{channelId}/channelPosts/{channelPostId}/channelContent")
    fun getChannelPostContent(@PathVariable channelId: Long,
                              @PathVariable channelPostId: Long): ResponseEntity<Any> {
        try {
            // s: validations
            val errorList = ArrayList<ErrorVo>()

            val foundedChannel = channelService.getChannelByChannelId(channelId)
            if (foundedChannel == null) {
                errorHelper.addErrorAttributes(BAD_REQUEST, "Not exits this Channel.", errorList)
            }

            if (errorList.isNotEmpty()) {
                val body = errorHelper.getErrorAttributes(errorList)
                return ResponseEntity.badRequest().body(body)
            }
            // e: validations
            val foundChannelPost = channelService.getChannelPostById(channelPostId)
            val foundChannelPostContent = channelService.getChannelPostContent(foundChannelPost)

            val channelPostContentDto = ChannelPostContentDto(channelPostId = foundChannelPost.channelPostId,
                    channelPostName = foundChannelPost.channelPostName,
                    accountId = foundChannelPost.accountId,
                    accountName = foundChannelPost.accountName,
                    channelPostContentId = foundChannelPostContent.channelPostContentId,
                    channelPostContent = foundChannelPostContent.channelPostContent,
                    createDate = foundChannelPostContent.createDate,
                    modifyDate = foundChannelPostContent.modifyDate
            )
            val channelPostContentEntityModel = ChannelPostContentEntityModel(channelPostContentDto)
            val selfLink = linkTo(methodOn(ChannelController::class.java).getChannelPostContent(channelId, channelPostId)).withSelfRel()
            channelPostContentEntityModel.add(selfLink)

            return ResponseEntity.ok(channelPostContentEntityModel)

        } catch (e: Exception) {
            log.error { e.message }
            val body = errorHelper.getUnexpectError(UNEXPECTED_ERROR_WORD)
            return ResponseEntity.badRequest().body(body)
        }
    }

    /**
     * Update ChannelPostContent.
     *
     * @author Ewan
     * @param channelId
     * @param channelPostId
     * @param channelPostDto
     * @return a ResponseEntity as satisfied with REST hateoas
     */
    @PatchMapping("{channelId}/channelPosts/{channelPostId}/channelContent")
    fun updateChannelPostContent(@PathVariable channelId: Long,
                                 @PathVariable channelPostId: Long,
                                 @RequestBody channelPostDto: ChannelPostDto): ResponseEntity<Any> {
        try {
            // s: validations
            val errorList = channelPostDtoValidator.validate(channelPostDto)

            val foundedChannel = channelService.getChannelByChannelId(channelId)
            if (foundedChannel == null) {
                errorHelper.addErrorAttributes(BAD_REQUEST, "Not exits this Channel.", errorList)
            }

            val foundedChannelPost = channelService.getChannelPostById(channelPostId)
            if (foundedChannelPost == null) {
                errorHelper.addErrorAttributes(BAD_REQUEST, "Not exits this Channel Post.", errorList)
            }

            if (errorList.isNotEmpty()) {
                val body = errorHelper.getErrorAttributes(errorList)
                return ResponseEntity.badRequest().body(body)
            }
            // e: validations

            val foundChannelPost = channelService.getChannelPostById(channelPostId)
            val foundChannelPostContent = channelService.getChannelPostContent(foundChannelPost)
            foundChannelPost.channelPostName = channelPostDto.channelPostName!!
            foundChannelPostContent.channelPostContent = channelPostDto.channelPostContent!!
            channelService.saveChannelPost(foundChannelPost)
            channelService.saveChannelPostContent(foundChannelPostContent)

            val channelPostContentDto = ChannelPostContentDto(channelPostId = foundChannelPost.channelPostId,
                    channelPostName = foundChannelPost.channelPostName,
                    accountId = foundChannelPost.accountId,
                    accountName = foundChannelPost.accountName,
                    channelPostContentId = foundChannelPostContent.channelPostContentId,
                    channelPostContent = foundChannelPostContent.channelPostContent,
                    createDate = foundChannelPostContent.createDate,
                    modifyDate = foundChannelPostContent.modifyDate
            )
            val channelPostContentEntityModel = ChannelPostContentEntityModel(channelPostContentDto)
            val selfLink = linkTo(methodOn(ChannelController::class.java).getChannelPostContent(channelId, channelPostId)).withSelfRel()
            channelPostContentEntityModel.add(selfLink)

            return ResponseEntity.ok(channelPostContentEntityModel)

        } catch (e: Exception) {
            log.error { e.message }
            val body = errorHelper.getUnexpectError(UNEXPECTED_ERROR_WORD)
            return ResponseEntity.badRequest().body(body)
        }
    }

    /**
     * Update viewCount of ChannelPostContent.
     *
     * @author Ewan
     * @param channelId
     * @param channelPostId
     * @param viewCount
     * @return a ResponseEntity as satisfied with REST hateoas
     */
    @PatchMapping("{channelId}/channelPosts/{channelPostId}/viewCount")
    fun updateChannelPostViewCount(@PathVariable channelId: Long,
                                   @PathVariable channelPostId: Long,
                                   @RequestBody viewCount: Long
                                 ): ResponseEntity<Any> {
        try {
            // s: validations
            val errorList = ArrayList<ErrorVo>()

            val foundedChannel = channelService.getChannelByChannelId(channelId)
            if (foundedChannel == null) {
                errorHelper.addErrorAttributes(BAD_REQUEST, "Not exits this Channel.", errorList)
            }

            val foundChannelPost = channelService.getChannelPostById(channelPostId)
            if (foundChannelPost == null) {
                errorHelper.addErrorAttributes(BAD_REQUEST, "Not exits this Channel Post.", errorList)
            }

            if (errorList.isNotEmpty()) {
                val body = errorHelper.getErrorAttributes(errorList)
                return ResponseEntity.badRequest().body(body)
            }
            // e: validations

            foundChannelPost.viewCount = viewCount
            foundChannelPost.modifyDate = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date())
            val savedChannelPost = channelService.saveChannelPost(foundChannelPost)

            val channelPostEntityModel = ChannelPostEntityModel(savedChannelPost)
            val selfLink = linkTo(methodOn(ChannelController::class.java).updateChannelPostViewCount(channelId, channelPostId, viewCount)).withSelfRel()
            channelPostEntityModel.add(selfLink)

            return ResponseEntity.ok(channelPostEntityModel)

        } catch (e: Exception) {
            log.error { e.message }
            val body = errorHelper.getUnexpectError(UNEXPECTED_ERROR_WORD)
            return ResponseEntity.badRequest().body(body)
        }
    }

    /**
     * Create new ChannelPostComment.
     *
     * @author Ewan
     * @param channelId
     * @param channelPostId
     * @param channelPostCommentDto
     * @return a ResponseEntity as satisfied with REST hateoas
     */
    @PostMapping("{channelId}/channelPosts/{channelPostId}/channelPostComments")
    fun createChannelPostComments(@PathVariable channelId: Long,
                                  @PathVariable channelPostId: Long,
                           @RequestBody channelPostCommentDto: ChannelPostCommentDto): ResponseEntity<Any> {
        try {
            // s: validations
            val errorList = channelPostCommentDtoValidator.validate(channelPostCommentDto)

            val foundedChannel = channelService.getChannelByChannelId(channelId)
            if (foundedChannel == null) {
                errorHelper.addErrorAttributes(BAD_REQUEST, "Not exits this Channel.", errorList)
            }

            val foundedChannelPost = channelService.getChannelPostById(channelPostId)
            if (foundedChannelPost == null) {
                errorHelper.addErrorAttributes(BAD_REQUEST, "Not exits this Channel Post.", errorList)
            }

            val foundedAccount= accountService.getAccountById(channelPostCommentDto.accountId?: -1) //prevent null point exception
            if (foundedAccount == null) {
                errorHelper.addErrorAttributes(BAD_REQUEST, "Not exits this account", errorList)
            }

            if (errorList.isNotEmpty()) {
                val body = errorHelper.getErrorAttributes(errorList)
                return ResponseEntity.badRequest().body(body)
            }
            // e: validations

            val channelPostComment = ChannelPostComment(
                    channelPostComment = channelPostCommentDto.channelPostComment,
                    accountId = channelPostCommentDto.accountId,
                    accountName = foundedAccount?.accountname!!,
                    channelPost = foundedChannelPost
            )

            val savedChannelPostComment = channelService.saveChannelPostComment(channelPostComment)

            val entityModel = savedChannelPostComment.run {
                val channelPostCommentEntityModel = ChannelPostCommentEntityModel(this, foundedChannelPost.channelPostId!!)
                val selfLink = linkTo(ChannelController::class.java).slash(channelId).slash("/channelPost").slash(channelPostId).slash("channelPostComments").slash(savedChannelPostComment.channelPostCommentId).withSelfRel()
                channelPostCommentEntityModel.add(selfLink)
            }

            val linkBuilder = linkTo(methodOn(ChannelController::class.java).createChannelPostComments(channelId, channelPostId, channelPostCommentDto))
            val createUrl = linkBuilder.toUri()

            return ResponseEntity.created(createUrl).body(entityModel)

        } catch (e: Exception) {
            val body = errorHelper.getUnexpectError(UNEXPECTED_ERROR_WORD)
            return ResponseEntity.badRequest().body(body)
        }
    }

    /**
     * Get ChannelPostComments.
     *
     * @author Ewan
     * @param channelId
     * @param channelPostId
     * @return a ResponseEntity as satisfied with REST hateoas
     */
    @GetMapping("{channelId}/channelPosts/{channelPostId}/channelPostComments")
    fun getChannelPostComments(@PathVariable channelId: Long,
                              @PathVariable channelPostId: Long): ResponseEntity<Any> {
        try {
            // s: validations
            val errorList = ArrayList<ErrorVo>()
 
            val foundedChannel = channelService.getChannelByChannelId(channelId)
            if (foundedChannel == null) {
                errorHelper.addErrorAttributes(BAD_REQUEST, "Not exits this Channel.", errorList)
            }

            val foundedChannelPost = channelService.getChannelPostById(channelPostId)
            if (foundedChannelPost == null) {
                errorHelper.addErrorAttributes(BAD_REQUEST, "Not exits this Channel Post.", errorList)
            }

            if (errorList.isNotEmpty()) {
                val body = errorHelper.getErrorAttributes(errorList)
                return ResponseEntity.badRequest().body(body)
            }
            // e: validations

            val channelComments = channelService.getChannelPostCommentByChannelPostId(channelPostId)

            val channelPostCommentsEntityModel = channelComments.map {
                val channelPostCommentModel = ChannelPostCommentEntityModel(it, channelPostId)
                val selfLink = linkTo(ChannelController::class.java).slash(channelId).slash("/channelPost").slash(channelPostId).slash("channelPostComments").slash(it.channelPostCommentId).withSelfRel()
                channelPostCommentModel.add(selfLink)
            }

            val selfLink = linkTo(methodOn(ChannelController::class.java).getChannelPostComments(channelId, channelPostId)).withSelfRel()
            val resultEntityModel = CollectionModel(channelPostCommentsEntityModel, selfLink)

            return ResponseEntity.ok(resultEntityModel)

        } catch (e: Exception) {
            val body = errorHelper.getUnexpectError(UNEXPECTED_ERROR_WORD)
            return ResponseEntity.badRequest().body(body)
        }
    }
}