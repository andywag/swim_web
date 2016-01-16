package org.simplifide.client.widgets

import org.scalajs.dom
import org.scalajs.dom.ext.SessionStorage
import org.scalajs.jquery._
import org.simplifide.client.widgets.SelectMenu.MenuItem
import org.simplifide.client.{Storable, DisplayElement}
import rx.core.{Rx, Var}

import scalatags.JsDom.all
import scalatags.JsDom.all._
//import upickle.default._

/**
  * Created by andy.wagner on 11/12/2015.
  */
case class SelectMenu(displayId:String, name1:String, items:List[MenuItem], initial:Int = 0) extends DisplayElement {


  val selection = Var(
    SessionStorage(displayId).map(x => items(x.toInt)).getOrElse(items(initial))
  )

  val value = Rx {
    selection().value
  }


  override def createHtmlElement: all.HtmlTag = {

    def createItem(index:Int,item:String) =
      if (index == selection().index) option(selected:="selected")(item.toString)
      else option(item.toString)
    div(
      label(`class`:=SelectPanel.SELECT_LABEL_STYLE)(name1),
      select(id:=displayId, name:=name1)(
        items.zipWithIndex.map(x => createItem(x._2, x._1.display))
      ),
      br()
    )
  }

  def create = {

    def call(event:dom.Event) = {
      val res = jQuery(s"#$displayId").`val`().toString
      val sel = items.find(x => x.toString.equalsIgnoreCase(res))
      selection.update(sel.get)
      sel.get.store(displayId)

    }
    jQuery(s"#${displayId}").change(call(_))

  }


}

object SelectMenu {


  trait MenuItem{
    val display:String
    /** Index of Item in Menu List */
    val index:Int
    /** Value of the Item */
    val value:Int

    override def toString = display
    def store(id:String)   = {
      SessionStorage.update(id,index.toString)

    }
  }

  case class DefaultMenuItem(val display:String, val value:Int, val index:Int) extends MenuItem

}

