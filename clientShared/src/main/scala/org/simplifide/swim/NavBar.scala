package org.simplifide.swim

import org.simplifide.client.DisplayElement

import scalatags.JsDom.all
import scalatags.JsDom.all._

/**
  * Created by andyw_000 on 11/23/2015.
  */
class NavBar extends DisplayElement{

  val displayId = "UnUsed"

  override def createHtmlElement: all.HtmlTag = {
    ul(`class`:="nav navbar-nav navbar-right")(
      li(a(href:="index")("Swimmers")),
      li(a(href:="#")("Teams")),
      li(a(href:="meetSummary")("Meets")),
      li(a(href:="#")("About"))
    )
  }

  override def create: Unit = {

  }
}
