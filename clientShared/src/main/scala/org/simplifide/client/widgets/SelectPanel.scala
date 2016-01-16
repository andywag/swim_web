package org.simplifide.client.widgets

import org.simplifide.client.DisplayElement
import org.simplifide.swim.widget.CommonMenu.MenuDetails

import scalatags.JsDom.all
import scalatags.JsDom.all._

/**
  * Created by Andy on 12/2/2015.
  */
trait SelectPanel {

  self:DisplayElement =>

  import SelectPanel._
  val menus:List[DisplayElement]

  def createSelectPanel(info:MenuDetails) = {
    new SelectMenu(createSubSection(info.id),info.title,info.items)
  }

  override def createHtmlElement: all.HtmlTag =
    div(id:=displayId)(
      menus.map(x =>
          x.createHtmlElement
      )
    )

  def create = menus.foreach(_.create)

}

object SelectPanel {
  val SELECT_LIST_STYLE  = "selectListStyle"
  val SELECT_ITEM_STYLE  = "selectItemStyle"
  val SELECT_LABEL_STYLE = "selectLabelStyle"

}
