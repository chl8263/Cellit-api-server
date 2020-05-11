package me.ewan.cellit.domain.cell.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity

@RestController
class CellController {

    @GetMapping("/cells/{account-id}")
    fun getCellsFromAccountId(): ResponseEntity<Unit>{
        val uri = linkTo(methodOn(CellController::class.java).getCellsFromAccountId()).slash("1").toUri()

        return ResponseEntity.created(uri).build()
    }

}