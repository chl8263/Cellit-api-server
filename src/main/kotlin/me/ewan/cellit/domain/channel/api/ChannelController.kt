package me.ewan.cellit.domain.channel.api

import me.ewan.cellit.domain.cell.vo.dto.validator.ChannelPostDtoValidator
import me.ewan.cellit.domain.channel.service.ChannelService
import me.ewan.cellit.domain.channel.vo.domain.ChannelPost
import me.ewan.cellit.domain.channel.vo.domain.ChannelPostContent
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import me.ewan.cellit.domain.channel.vo.dto.ChannelPostDto
import me.ewan.cellit.domain.channel.vo.entityModel.ChannelEntityModel
import me.ewan.cellit.domain.channel.vo.entityModel.ChannelPostEntityModel
import me.ewan.cellit.global.error.ErrorHelper
import me.ewan.cellit.global.error.vo.ErrorVo
import me.ewan.cellit.global.error.vo.HTTP_STATUS.BAD_REQUEST
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/channels"], produces = [MediaTypes.HAL_JSON_VALUE])
class ChannelController {

    private val log = KotlinLogging.logger {}

    @Autowired
    lateinit var channelService: ChannelService

    @Autowired
    lateinit var modelMapper: ModelMapper

    @Autowired
    private lateinit var errorHelper: ErrorHelper

    @Autowired
    private lateinit var channelPostDtoValidator: ChannelPostDtoValidator

    @PostMapping
    fun createChannel(@RequestBody channelDto: ChannelDto): ResponseEntity<Any>{
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

        }catch (e: Exception){
            val body = errorHelper.getUnexpectError("Please try again..")
            return ResponseEntity.badRequest().body(body)
        }
    }

    @PostMapping("{channelId}/channelPosts")
    fun createChannelPosts(@PathVariable channelId: Long,
                           @RequestBody channelPostDto: ChannelPostDto): ResponseEntity<Any>{
        try {
            // s: validator
            val errorList = channelPostDtoValidator.validate(channelPostDto)

            val foundedChannel = channelService.getChannelWithChannelId(channelId)
            if(foundedChannel == null){
                errorHelper.addErrorAttributes(BAD_REQUEST, "Not exits this Channel.", errorList)
            }

            if (errorList.isNotEmpty()) {
                val body = errorHelper.getErrorAttributes(errorList)
                return ResponseEntity.badRequest().body(body)
            }
            // e: validator

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

        }catch (e: Exception){
            log.error { e.message }
            println(e)
            val body = errorHelper.getUnexpectError("Please try again..")
            return ResponseEntity.badRequest().body(body)
        }
    }

    @GetMapping("{channelId}/channelPosts")
    fun getChannelPost(@PathVariable channelId: Long,
                       pageable: Pageable,
                       assembler: PagedResourcesAssembler<ChannelPost>): ResponseEntity<Any>{
        try {
            // s: validator
            val foundChannel = channelService.getChannelWithChannelId(channelId)
            if(foundChannel == null){
                val body = errorHelper.getUnexpectError("Not exits this Channel.")
                return ResponseEntity.badRequest().body(body)
            }
            // e: validator

            val page = this.channelService.getChannelPosts(channelId, pageable)
            val pageEntityModel = assembler.toModel(page) { entity: ChannelPost ->
                val channelPostEntityModel = ChannelPostEntityModel(entity)
                val selfLink = linkTo(ChannelController::class.java).slash(channelId).slash("/channelPosts").slash(entity.channelPostId).withSelfRel()
                channelPostEntityModel.add(selfLink)
            }

            return ResponseEntity.ok(pageEntityModel)
        }catch (e: Exception){
            val body = errorHelper.getUnexpectError("Please try again..")
            return ResponseEntity.badRequest().body(body)
        }

    }
}