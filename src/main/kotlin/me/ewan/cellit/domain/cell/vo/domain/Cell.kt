package me.ewan.cellit.domain.cell.vo.domain

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.jpa.repository.Temporal
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*

@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class, property = "cellId")
class Cell(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var cellId: Long? = null,

        var cellName: String,

        // default fetch type = LAZY
        @OneToMany(mappedBy = "cell", fetch = FetchType.LAZY)
        var accounts: MutableList<AccountCell> = mutableListOf(),

        //@CreationTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        //@CreationTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        var modifyDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        var active: Int = 1
)
//{
//       override fun toString() = kotlinToString(properties = toStringProperties)
//
//       override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)
//
//       override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)
//
//
//       companion object {
//              private val equalsAndHashCodeProperties = arrayOf(Cell::cellId)
//              private val toStringProperties = arrayOf(
//                      Cell::cellId,
//                      Cell::cellName
//              )
//       }
//}
