package org.simplifide.client.table

import org.simplifide.client.DisplayElement
import org.simplifide.client.table.AnBaseTable.ColumnGroup
import rx.core.{Obs, Var, Rx}
import scalatags.JsDom.all._

/**
  * Created by andy.wagner on 1/14/2016.
  */
trait AnBaseTable[T,U] extends DisplayElement  {

  val displayId:String


  val indexGroup:ColumnGroup[U]
  val groups:Rx[List[ColumnGroup[T]]]
  val rxData:Rx[List[List[T]]]
  val visible:Rx[List[Boolean]]


  lazy val filteredGroups = Rx{
    (groups() zip visible()).filter(x => x._2).map(x => x._1)
  }
  lazy val columns = Rx {filteredGroups().flatMap(x => x.columns)}


  val options:String = "table table-bordered table-striped table-condensed"
  //val options = ""



  /** Returns the HTML which is created for this element */
  def createHtmlElement = {
    def getMainHead   = tr(indexGroup.headTag, filteredGroups().map(x => x.headTag))
    def getSecondHead = tr(indexGroup.columns.map(x => x.headTag), columns().map(x => x.headTag))

    def createBody = {
      def createRow(x:List[T]) = {
        val displayedGroups = ((groups() zip x) zip visible()).filter(x => x._2).map(x => x._1)  // Filter the Data
        displayedGroups.flatMap(y => y._1.displayTag(y._2))                                      // Create the Tag
      }
      rxData().zipWithIndex.map(x => tr(td(x._2),createRow(x._1)))
    }

    div(`class`:="meetTableDiv")(
      table(id := displayId, `class` := options)(
        thead(getMainHead, getSecondHead),
        tbody(createBody)
      )
    )
  }


  def create = {

  }

  /** Attach an observer to the columns */





}

object AnBaseTable {

  trait Column[T] {
    val title: String
    def display(item:T):Any = ""
    def headTag = th(title)

    def displayTag(item:T):Tag = td(display(item).toString)
  }

  trait ColumnGroup[T] {
    def title:String
    val columns:List[Column[T]]
    def headTag              = th(colspan:=columns.length)(title)
    def displayTag(y:T)      = columns.map(x => x.displayTag(y))
  }


}


