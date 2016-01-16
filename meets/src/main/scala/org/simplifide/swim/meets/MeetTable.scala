package org.simplifide.swim.meets

import org.scalajs.dom
import org.simplifide.client.DisplayElement
import org.simplifide.client.datatable.BaseTable
import org.simplifide.client.widgets.DataTableWidget
import org.simplifide.swim.WebConstants
import org.simplifide.swim.meets.CompositeSearchPanel.State
import org.simplifide.swim.model.ModelWrapper.PersonWithEntry
import org.simplifide.swim.model.{Person, Team, Race, Entry}
import rx.core.{Obs, Rx}

import scala.collection.immutable.ListMap
import scala.scalajs.js
import scala.scalajs.js.Dictionary
import scalatags.JsDom.all
import scalatags.JsDom.all._

/**
  * Created by Andy on 1/10/2016.
  */
class MeetTable(val data:Rx[Map[String,List[Entry]]],
                val people:Rx[Map[String,PersonWithEntry]],
                val teams:Rx[Map[String,Team]],
                val searchState:State
               ) extends DisplayElement with DataTableWidget {
  val displayId:String = "meetTable"

  val sortedData = Rx {
    val res = ListMap(data().toSeq.sortBy(_._1):_*)
    res
  }

  val displayedColumns = Rx {
    sortedData().map(x => {
      val entry = x._2(0)
      val gender = searchState.gender() match {
        case 2 => true
        case 1 => if (entry.r.gender == Person.Male.value) false else true
        case 0 => if (entry.r.gender == Person.Male.value) true  else false
      }
      gender
    })
  }

  Obs(displayedColumns) {
    if (sortedData().size > 0) {
      clear(WebConstants.topColumn)
      val div = createElement
      getLocation(WebConstants.topColumn).map(x => {
        x.appendChild(div.render)
      })
    }
  }




  def createElement = {


    def getHead = {
      def columnText(r:Race) = th(colspan:=3)(s"${r.eNumber} ${r.n} ${r.c.displayString}")
      def second(x:Int) = if (x % 3 == 0) th("Name") else if (x % 3 == 1) th("Team") else th("Time")
      List(
        tr(th(colspan:=2)("Position"),sortedData().toList.map(x => columnText(x._2(0).r))),
        tr(th("Rank"),th("Place"),List.tabulate(2*sortedData().size)(x => second(x)))
        )
    }
    def createRows = {
      def columnText(entries:List[Entry], index:Int) = {
        def getName(id:String) = people().get(id).map(x => x.person.n.displayString).getOrElse("")
        def getTeam(id:String) = people().get(id).map(x => x.person.teamId).getOrElse("")

        def entryName(e:Entry) =  td(s"${getName(e.p)}")
        def entryTeam(e:Entry) =  td(s"${getTeam(e.p)}")
        def entryTime(e:Entry) =  td(s"${e.d.getFastestTime.toString}")

        if (index < entries.length) {
          val base = entries(index)
          (entryName(base),entryTeam(base),entryTime(base))
        }
        else (td("N"),td("N"),td("N"))
      }
      val max = sortedData().map(x => x._2.length).max
      List.tabulate(max)(z => {
        val data = sortedData().toList.zipWithIndex.map(x => columnText(x._1._2,z)).flatMap(x => List(x._1,x._2,x._3))
        val sdata = data
        tr(td(z),td(z),sdata)
      })
    }

  div(`class`:="meetTableDiv")(
//      table(id := displayId, `class` := "compact cell-border")(
        table(id := displayId, `class` := "table table-bordered table-striped table-condensed")(
          thead(getHead),
          tbody(createRows)
        )
  )

}

//val displayId:String
override def createHtmlElement: all.HtmlTag = {

div()



}

override def create: Unit = {

}

override val options = js.Dictionary(("scrollX",true),("scrollY",400),("paging",false),("autoWidth",false))
}
