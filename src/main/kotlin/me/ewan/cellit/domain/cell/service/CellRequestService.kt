package me.ewan.cellit.domain.cell.service

import me.ewan.cellit.domain.cell.dao.CellRequestRepository
import me.ewan.cellit.domain.cell.vo.domain.CellRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CellRequestService {

    @Autowired
    private lateinit var cellRequestRepository: CellRequestRepository

    fun getCellRequestWithId(id: Long) = cellRequestRepository.getOne(id)

    fun createCellRequest(cellRequest: CellRequest) = cellRequestRepository.save(cellRequest)

    fun findCellRequestsWithCellIdAndAccountId(cellId: Long, accountId: Long): CellRequest?{
        return cellRequestRepository.findCellRequestsWithCellIdAndAccountId(cellId, accountId)
    }
}