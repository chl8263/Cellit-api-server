package me.ewan.cellit.domain.cell.api

import me.ewan.cellit.domain.cell.model.CellModel
import me.ewan.cellit.domain.cell.service.CellService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class CellController {

    @Autowired
    private lateinit var cellService: CellService

    @GetMapping("/cells/{accountId}")
    fun getCellsFromAccountId(@PathVariable accountId: String): ResponseEntity<CellModel>{
        //val uri = linkTo(methodOn(CellController::class.java).getCellsFromAccountId()).slash("1").toUri()
        val uri = linkTo(CellController::class.java).toUri()
        return ResponseEntity.created(uri).body(CellModel())
    }

    @PostMapping("/cells/a")
    fun testCellPost(): Map<String, String>{

        return mapOf("test" to "testSuccess")
    }
}