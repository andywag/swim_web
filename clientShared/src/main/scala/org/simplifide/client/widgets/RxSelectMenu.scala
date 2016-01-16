package org.simplifide.client.widgets

import org.scalajs.dom
import org.scalajs.dom.ext.SessionStorage
import org.scalajs.jquery._
import org.simplifide.client.{SimpleElement, DisplayElement}
import org.simplifide.client.widgets.SelectMenu.MenuItem
import org.simplifide.client.datatable.DataTable
import rx.core.{Obs, Rx, Var}

import scala.scalajs.js
import scalatags.JsDom.all
import scalatags.JsDom.all._
//import upickle.default._

/**
  * Created by andy.wagner on 11/12/2015.
  */
case class RxSelectMenu(displayId:String, name1:String, items:Rx[List[MenuItem]], initial:Int = 0) extends DisplayElement {

  lazy val initialItem = if (items().size > initial) items()(initial) else items()(0)

  def clearSelection = {
    selection.update(items()(0))
  }

  def getItem(index:Int) = {
    val t = if (index < items().size ) items()(index) else initialItem
    t
  }

  val selection = Var {
    val store =  SessionStorage(displayId)
    val r= store.map(x => getItem(x.toInt)).getOrElse(initialItem)
    r
  }

  def createOption(item:MenuItem,index:Int) = {

    val r = selection.toTry.map(x => {
      if (index == selection().index) option(selected:="selected")(item.toString)
      else option(item.toString)
    }).getOrElse(option(item.toString))

    r

  }

  Obs(items) {
    val temp = jQuery(s"#$displayId")
    temp.empty()

    items().zipWithIndex.foreach(x => {
      temp.append(createOption(x._1,x._2).render)
    })
    //clearSelection
  }




  override def createHtmlElement: all.HtmlTag = {

    def createItem(index:Int,item:String) =
      if (index == selection().index) option(selected:="selected")(item.toString)
      else option(item.toString)
    fieldset(
      label(`class`:=SelectPanel.SELECT_LABEL_STYLE)(name1),
      select(id:=displayId, `class`:=SelectPanel.SELECT_ITEM_STYLE,name:=name1)(
        items().zipWithIndex.map(x => createItem(x._2, x._1.display))
      )
    )
  }

  def create = {

    def call(event:dom.Event) = {
      val res = jQuery(s"#$displayId").`val`().toString
      val sel = items().find(x => x.toString.equalsIgnoreCase(res))
      selection.update(sel.get)
      sel.get.store(displayId)
      val store =  SessionStorage(displayId)
    }
    jQuery(dom.document).ready {
      val temp = DataTable.jQuery(s"#${displayId}")
      jQuery(s"#${displayId}").change(call(_))
    }
  }
}
object RxSelectMenu {}
