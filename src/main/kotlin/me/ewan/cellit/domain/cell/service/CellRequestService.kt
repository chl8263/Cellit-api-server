package me.ewan.cellit.domain.cell.service

import me.ewan.cellit.domain.cell.dao.CellRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CellRequestService {

    @Autowired
    lateinit var cellRequestRepository: CellRequestRepository

    fun getCellRequestWithId(id: Long) = cellRequestRepository.getOne(id)
}