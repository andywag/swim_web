package org.simplifide.swim.meets

import org.scalajs.dom
import org.simplifide.client.DisplayElement
import org.simplifide.client.table.AnBaseTable
import org.simplifide.client.table.AnBaseTable.{Column, ColumnGroup}
import org.simplifide.client.widgets.DataTableWidget
import org.simplifide.swim.WebConstants
import org.simplifide.swim.meets.CompositeSearchPanel.State
import org.simplifide.swim.meets.MeetTable2.{IndexColumnGroup, Index, TimeColumnGroup, TimeGroup}
import org.simplifide.swim.model.ModelWrapper.PersonWithEntry
import org.simplifide.swim.model.{Entry, Person, Race, Team}
import rx.core.{Obs, Rx}

import scala.collection.immutable.ListMap
import scala.scalajs.js
import scalatags.JsDom.all
import scalatags.JsDom.all._

/**
  * Created by Andy on 1/10/2016.
  */
class MeetTable2(val data:Rx[Map[String,List[Entry]]],
                 val people:Rx[Map[String,PersonWithEntry]],
                 val teams:Rx[Map[String,Team]],
                 val searchState:State
               ) extends DisplayElement with AnBaseTable[TimeGroup, Index] {

  val displayId: String = "meetTable"
  val indexGroup        = IndexColumnGroup

  val sortedData = Rx {
    val res = ListMap(data().toSeq.sortBy(_._1): _*)
    res
  }


  val visible:Rx[List[Boolean]] = Rx {
    val dat = sortedData().map(x => {
      val entry = x._2(0)

      val gender = searchState.gender() match {
        case 2 => true
        case 1 => if (entry.r.gender == Person.Male.value) false else true
        case 0 => if (entry.r.gender == Person.Male.value) true else false
      }

      val stroke = searchState.stroke() match {
        case 0 => true
        case x => (x == entry.r.c.t)
      }

      val distance = searchState.distance() match {
        case 0 => true
        case x => (x == entry.r.c.l)
      }

      val age = searchState.age() match {
        case 0 => true
        case 1 => if (entry.r.n.equalsIgnoreCase("UN08")) true else false
        case 2 => if (entry.r.n.equalsIgnoreCase("UN10")) true else false
        case 3 => if (entry.r.n.equalsIgnoreCase("1112")) true else false
        case 4 => if (entry.r.n.equalsIgnoreCase("1314")) true else false
        case 5 => if (entry.r.n.equalsIgnoreCase("1516") | entry.r.n.equalsIgnoreCase("1518")) true else false
        case 6 => if (entry.r.n.equalsIgnoreCase("1718") | entry.r.n.equalsIgnoreCase("1518")) true else false
        case 7 => if (entry.r.n.equalsIgnoreCase("UNOV")) true else false
        case _ => true
      }
     // dom.console.warn(searchState.toString)
     // dom.console.warn(s"$gender $stroke $distance $age")

      gender & stroke & distance & age
    })
    dat.toList
  }

  Obs (visible, skipInitial = true) {
    //dom.console.warn(visible().toString())
    this.redraw(WebConstants.topColumn)
  }


  def createRows: List[List[TimeGroup]] = {
    def columnText(entries: List[Entry], index: Int) = {
      def getName(id: String) = people().get(id).map(x => x.person.n.displayString).getOrElse("")
      def getTeam(id: String) = people().get(id).map(x => x.person.teamId).getOrElse("")

      if (index < entries.length) {
        val base = entries(index)
        new TimeGroup(getName(base.p), getTeam(base.p), base.d.getFastestTime.toString)
      }
      else TimeGroup("N", "N", "N")
    }
    val max = sortedData().map(x => x._2.length).max

    val rows = List.tabulate(max)(z => {
      val data = sortedData().toList.zipWithIndex.map(y => columnText(y._1._2, z))
      data
    })
    rows
  }

  override val groups: Rx[List[ColumnGroup[TimeGroup]]] = Rx {
    def columnText(r:Race) = s"${r.eNumber} ${r.n} ${r.c.displayString}"
    val gr = sortedData().map(x => new TimeColumnGroup(columnText(x._2(0).r))).toList
    gr
  }
  override val rxData: Rx[List[List[TimeGroup]]] = Rx {
    val rows = createRows
    rows

  }

  /*
  Obs (displayedColumns, skipInitial = true) {
    this.clear(WebConstants.topColumn)
    this.attach(WebConstants.topColumn)
  }
  */

  Obs (rxData, skipInitial = true) {
    this.clear(WebConstants.topColumn)
    this.attach(WebConstants.topColumn)
  }

  /*

  Obs(displayedColumns) {
    if (sortedData().size > 0) {
      clear(WebConstants.topColumn)
      val div = createElement
      getLocation(WebConstants.topColumn).map(x => {
        x.appendChild(div.render)
      })
    }
  }
  */

}


object MeetTable2 {
  case class TimeGroup(name:String, team:String, time:String)
  case class Index(rank:Int, position:Int)

  object NameColumn       extends Column[TimeGroup] {
    val title: String = "Name"
    override def headTag = th(`class`:="meetName")(title)
    override def displayTag(item:TimeGroup):Tag = td(item.name)
  }

  object TeamColumn       extends Column[TimeGroup] {
    val title: String = "Team"
    override def headTag = th(`class`:="meetTeam")(title)
    override def displayTag(item:TimeGroup):Tag = td(item.team)
  }

  object TimeColumn       extends Column[TimeGroup] {
    val title: String = "Time"
    override def headTag = th(`class`:="meetTime")(title)
    override def displayTag(item:TimeGroup):Tag = td(item.time)
  }

  class TimeColumnGroup(name:String) extends ColumnGroup[TimeGroup] {
    def title:String = name
    val columns:List[Column[TimeGroup]] = List(NameColumn, TeamColumn, TimeColumn)
  }

  object RankColumn extends Column[Index] {
    val title:String = "Rank"
    override def headTag = th(`class`:="meetRank")(title)
    override def displayTag(item:Index):Tag = td(item.rank)
  }

  object PositionColumn extends Column[Index] {
    val title:String = "Place"
    override def displayTag(item:Index):Tag = td(item.position)
  }

  object IndexColumnGroup extends ColumnGroup[Index] {
    def title:String = ""
    val columns:List[Column[Index]] = List(RankColumn)
  }

}
