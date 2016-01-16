package org.simplifide.swim.swimmer

import org.scalajs.dom
import org.simplifide.client.DisplayElement
import org.simplifide.client.widgets.DataTableWidget
import org.simplifide.client.datatable.{InternalTable, DataTable}
import org.simplifide.swim.TimeMeasureTable.TimeStandard
import org.simplifide.swim.model.ModelWrapper.{PersonWithEntryList, PersonWithEntry}
import org.simplifide.swim.model._
import rx._

import scala.scalajs.js
import scalatags.JsDom.all
import scalatags.JsDom.all._
import scalatags.generic.Attr
import org.scalajs.jquery.{JQueryEventObject, jQuery}

/**
  * Created by andyw_000 on 11/25/2015.
  */
class SwimmerTable2(val baseEntries:Rx[PersonWithEntryList],
                    val menuState:RaceSelectionPanel.State) extends DisplayElement with DataTableWidget {

  val displayId = "swimmerTable"
  val code      = Var[Int](-1)

  Obs(menuState.display,skipInitial = true) {
    redrawTable
  }

  val entries = Rx {
    baseEntries().entries
  }

  /** Not needed because it is generated in template */
  override def createHtmlElement: all.HtmlTag = div()

  /** Options for the base table */
  val options = js.Dictionary(("scrollX",true),("scrollY",200),("paging",false))

  def handleIn(event:JQueryEventObject) = {
    val element = event.currentTarget.asInstanceOf[dom.Element]
    val ucode = element.getAttribute("code")

    if (!ucode.equals("null") && ucode.toInt != code()) {
      code.update(ucode.toInt)
      dom.console.warn("Updating " + ucode)
    }
    //dom.console.warn(th)
    null
  }
  def handleOut(event:JQueryEventObject) = {
   // dom.console.warn(event)
    null
  }

  override def create: Unit = {
    org.scalajs.jquery.jQuery(dom.document).ready{
      internalTable
    }
  }

  val meetList = Rx {
    val e1 = entries().groupBy(x => x.m).toList
    // TODO put back the sort routine
    //val e2 = e1.sortBy(x => x._1.date)
    val e2 = e1
    e2
  }

  val codes = Rx {
    entries().map(x => x.r.c).toSet.toList.sorted
  }


  val cells = Rx {
    def createRow(x:(String,Iterable[Entry])) = {
      def getRace(code:Code):Either[Entry,Code] = {
        x._2.find(y => code == y.r.c).map(y => Left(y)).getOrElse(Right(code))
      }
      codes().map(y => getRace(y))
    }
    val row = meetList().map(x => (x._1,createRow(x)))
    //dom.console.warn("Here" + row)
    row
  }

  /*
  val cells = Rx {
    def createRow(x:(String,Iterable[Entry])) = {
      codes().map(y => x._2.find(z => z.race.code == y))
    }
    val row = meetList().map(x => (x._1,createRow(x)))
    dom.console.warn("Here" + row)
    row
  }
  */



  val measureFilter = Rx {
    val r = codes().zipWithIndex.map(x => {
      val up = menuState.measure() match {
        case Time.NONE => true
        case Time.LCM => x._1.measure == Time.LCM
        case Time.SCY => x._1.measure == Time.SCY
      }
      up
    })
    r
  }

  val distanceFilter = Rx {
    val r = codes().zipWithIndex.map(x => {
      if (menuState.distance() == 0) true
      else (x._1.length == menuState.distance())
    })
    r
  }

  val strokeFilter = Rx {
    val r = codes().zipWithIndex.map(x => {
      if (menuState.stroke() == 0) true
      else (x._1.typ == menuState.stroke())
    })
    r
  }

  val finalFilter = Rx {
    measureFilter().zipWithIndex.map(x => {
      x._1 & distanceFilter()(x._2) & strokeFilter()(x._2)
    })
  }




  val filteredCells = Rx {
    val c = finalFilter()
    def pass(x:(String,List[Either[Entry,Code]])) = {
      def cEntry(e:Either[Entry,Code]) = e match {
        case Right(_) => false
        case _        => true
      }
      val r = x._2.zipWithIndex.map(y => if (c(y._2)) cEntry(y._1) else false)
      val rr = r.foldLeft(false)(_|_)
      rr
    }
    val t = cells().filter(x => pass(x))
    t
  }

  //Obs(finalFilter)
  Rx{
      finalFilter().zipWithIndex.foreach(x => {
        if (x._2 >= 0) {
          internalTable.column(x._2+1).visible(x._1,false)
        }
        internalTable.columns.adjust()
      })
  }


  Obs (filteredCells) {
      this.redrawTable
  }

  def redrawTable = {
    def createRow(ent:(String,List[Either[Entry,Code]])) = {
      def createCell(ent:Entry) = {
        val c = TimeConverter.timeStandardByPersonEntry(ent.r.c,baseEntries().person,Some(ent))
        val d = TimeConverter.createDisplayTag(c,menuState.display())
        d
      }
      // TODO : Fix the Null Asspect of the Get Or Else to Include "code"

      val times = ent._2.map(x => {
        x match {
          case Left(x)   => createCell(x)
          case Right(x)  => td(Attr("code"):=x.codeString)("N")
        }
      })
      val meet = MeetHolder(ent._1).map(x => Utils.output(x.date)).getOrElse("")
      val row   = tr(td(meet/*Utils.output(ent._1.date)*/) :: times)
      row
    }
    internalTable.clear()
    filteredCells().foreach(x => {
      internalTable.row.add(createRow(x).render)
    })
    internalTable.draw()
    jQuery(s"#${displayId} tbody td").hover(handleIn _, handleOut _)

  }

  sealed trait EntryHolder
  case class Ent(entry:Entry) extends EntryHolder
  case class Not(code:Code)   extends EntryHolder


}


