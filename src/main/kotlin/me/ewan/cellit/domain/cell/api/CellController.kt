package me.ewan.cellit.domain.cell.api

import me.ewan.cellit.domain.account.api.AccountController
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.cell.model.CellDto
import me.ewan.cellit.domain.cell.model.CellModel
import me.ewan.cellit.domain.cell.service.CellService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/cells"], produces = [MediaTypes.HAL_JSON_VALUE])
class CellController {

    @Autowired
    private lateinit var cellService: CellService

    @Autowired
    private lateinit var accountService: AccountService

//    @GetMapping("/{accountId}")
//    fun getCellsFromAccountId(@PathVariable accountId: Long): ResponseEntity<CellModel>{
//
//        val cells = accountService.getCellsWithAccountId(accountId)
//
//        val cellModel = CellModel(cells[0])
//
//        return ResponseEntity.ok(cellModel)
//    }

    @PostMapping
    fun createCell(@RequestBody cellDto: CellDto): ResponseEntity<CellModel> {

        println("!@!@!@!")



//        val cells = accountService.getCellsWithAccountName(accountName)
//
//        val cellModel = CellModel(cells)
//        cellModel.add(linkTo(WebMvcLinkBuilder.methodOn(AccountController::class.java).getCellsFromAccountId(accountName)).slash(accountName).slash("cells").withSelfRel())

        return ResponseEntity.ok().build()
    }

//    @PostMapping("/test")
//    fun testCellPost(model: Model): Map<String, String>{
//
//        return mapOf("test" to "testSuccess")
//    }
}