package me.ewan.cellit.domain.cell.dao.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import me.ewan.cellit.domain.cell.dao.CellRequestDslRepository
import me.ewan.cellit.domain.cell.vo.domain.CellRequest
import me.ewan.cellit.domain.cell.vo.domain.QCell
import me.ewan.cellit.domain.cell.vo.domain.QCellRequest
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.PersistenceContext

class CellRequestDslRepositoryImpl : QuerydslRepositorySupport(CellRequest::class.java), CellRequestDslRepository {

//    @PersistenceContext
//    private lateinit var em: EntityManager

    override fun findCellRequestsWithCellIdAndAccountId(cellId: Long, accountId: Long): CellRequest? {

        val cellRequest = QCellRequest.cellRequest
        val cell = QCell.cell

        return from(cellRequest)
                .where(cell.cellId.eq(cellId)
                        .and(cellRequest.accountId.eq(accountId)))
                .fetchOne()

        //val qf = JPAQueryFactory(em)

        //
//        return qf.selectFrom(cellRequest)
//                .where(cell.cellId.eq(cellId)
//                        .and(cellRequest.accountId.eq(accountId)))
//                .fetchOne()
    }

    override fun findByCellIdOrderByCreateDate(cellId: Long): List<CellRequest> {
        val cellRequest = QCellRequest.cellRequest
        val cell = QCell.cell

        return from(cellRequest)
                .where(cell.cellId.eq(cellId))
                .orderBy(cellRequest.createDate.desc())
                .fetch()
    }
}