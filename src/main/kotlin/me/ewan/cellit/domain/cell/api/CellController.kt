package me.ewan.cellit.domain.cell.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@RestController
@RequestMapping("/api")
class CellController {

    @GetMapping("/cells/{account-id}")
    fun getCellsFromAccountId(): ResponseEntity<Unit>{
        val uri = linkTo(methodOn(CellController::class.java).getCellsFromAccountId()).slash("1").toUri()

        return ResponseEntity.created(uri).build()
    }

    @PostMapping("/cells/1")
    fun testCellPost(): String{

        return "test"
    }

}