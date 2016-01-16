package org.simplifide.client.datatable

import org.scalajs.dom
import org.scalajs.dom.ext.SessionStorage
import org.simplifide.client.DisplayElement
import org.simplifide.client.widgets.DataTableWidget
import org.simplifide.swim.model.{PersonWithTimes, Person}
import rx.core.{Dynamic, Rx, Obs, Var}

import scala.concurrent.ExecutionContext
import scala.scalajs.js
import scalatags.JsDom.all._
import scala.scalajs.js.JSConverters._
import org.scalajs.jquery.jQuery
/**
  * Created by andy.wagner on 11/18/2015.
  */
trait BaseTable[T] extends DisplayElement with DataTableWidget {

  val displayId:String
  val columns:Rx[List[Column]]

  val rxData:Rx[Iterable[T]]
  val active:Rx[Boolean]
  val indexColumn = true

  // Store the Order of the Table
  //val orderIndex =  createSubSection("Order") //s"${displayId}_order"

  def setColumnVisible(column:Int,visible:Boolean) = {
    if (active()) {
      val col = internalTable.column(column)
      col.visible(visible)
    }
  }

  lazy val filteredDataUnOrder = Rx {
    if (active()) {
      val filters = columns().map(x => x.filterItem())
      var result = rxData()
      for (filter <- filters) {
        result = result.flatMap(filter(_))
      }
      result
    }
    else List()
  }

  // TODO : Need Ordering Before Removing Values for Display
  lazy val filteredData = Rx {
    if (active()) {
      filteredDataUnOrder()
    }
    else List()
  }


  val options:js.Dictionary[AnyVal] = js.Dictionary(("responsive",true),("scrollX",true),("scrollY",500),("paging",false))


  def createIndexRow = {
    def handle() = {
      val t = internalTable.column(0,js.Dictionary(("order","applied")))//.nodes()
      t.nodes().zipWithIndex.foreach(x => x._1.innerHTML = s"<td>${x._2+1}</td>")
      val ord = internalTable.settings().order()(0).toString
      // Store the Ordering when the index row is updated
      SessionStorage.update(orderIndex, ord)
    }
    internalTable.on("order.dt searh.dt",handle _)
  }


  /** Returns the HTML which is created for this element */
  def createHtmlElement = {
    def getColumns = {
      val base = columns().map(x => th(x.title))
      if (indexColumn) tr(th() :: base) else tr(base)
    }
    div(
      table(id := displayId, `class` := "compact")(
        thead(getColumns),
        tbody()
      )
    )
  }

  def clearTable = {
    internalTable.clear()
  }
  /** Attaches the HTML tag to a dom location */


  def redrawTable() = {

    def getColumn(x:(T,Int)) = {

      val base = columns().map(y => {
        y.displayTag(x._1)
      })

      if (indexColumn) tr(td(x._2.toString) :: base) else tr(base)
    }

    if (active()) {

      internalTable.clear()
      val limitData = if (filteredData().size > 500) filteredData().slice(0,500) else filteredData()
      val tds = limitData.zipWithIndex.map(x => getColumn(x))
      tds.foreach(x => internalTable.row.add(x.render))
      internalTable.draw()
    }
  }


  val initialOrder = ("4,desc")
  def onCreate:Unit = {
    val x = SessionStorage(orderIndex).getOrElse(initialOrder)
    val ind0 = x.split(",")
    internalTable.order(Array(ind0(0).toInt,ind0(1)).toJSArray)

  }

  def create = {

    internalTable

    if (indexColumn) createIndexRow
    this.onCreate

    /** Attach an observer to the data */
    Obs(filteredData, skipInitial = true) {
      redrawTable()
    }
  }

  /** Attach an observer to the columns */




  trait Column {
    val title: String
    def display(item:T):Any = ""
    def displayTag(item:T):Tag = td(display(item).toString)

    //def filter(item:T):Option[T] = Some(item)
    lazy val filterItem:Rx[(T)=>Option[T]] = Var((x)=>Some(x))
  }
  def simple[T](name:String) = new Column {val title = name}

}


