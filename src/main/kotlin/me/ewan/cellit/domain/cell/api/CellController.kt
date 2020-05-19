package me.ewan.cellit.domain.cell.api

import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.cell.model.CellModel
import me.ewan.cellit.domain.cell.service.CellService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cells")
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

    @PostMapping("/test")
    fun testCellPost(): Map<String, String>{

        return mapOf("test" to "testSuccess")
    }
}