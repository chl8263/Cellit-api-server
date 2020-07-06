package me.ewan.cellit.domain.cell.vo.domain

import me.ewan.cellit.domain.cell.vo.domain.Cell
import org.springframework.data.jpa.repository.Temporal
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*

@Entity
class CellRequest(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var cellRequestId: Long? = null,

        @ManyToOne
        @JoinColumn(name = "cellId")
        var cell: Cell,

        @Column
        var accountId: Long? = null,

        @Temporal(TemporalType.TIMESTAMP)
        var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date())
)