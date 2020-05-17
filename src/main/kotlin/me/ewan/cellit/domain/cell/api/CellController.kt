package me.ewan.cellit.domain.cell.api

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class CellController {

    @GetMapping("/cells/{accountId}")
    fun getCellsFromAccountId(@PathVariable accountId: String): ResponseEntity<Unit>{
        //val uri = linkTo(methodOn(CellController::class.java).getCellsFromAccountId()).slash("1").toUri()
        val uri = linkTo(CellController::class.java).toUri()
        return ResponseEntity.created(uri).build()
    }

    @PostMapping("/cells/a")
    fun testCellPost(): Map<String, String>{

        println("><><><><><><")

        return mapOf("test" to "testSuccess")
    }

}