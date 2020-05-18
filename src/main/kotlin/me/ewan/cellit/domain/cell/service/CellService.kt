package me.ewan.cellit.domain.cell.service

import me.ewan.cellit.domain.cell.dao.CellRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CellService {

    @Autowired
    lateinit var cellRepository: CellRepository

    fun getCellsWithAccountId(accountId: Long){
        cellRepository.findById(accountId)
    }
}