package org.simplifide.swim.swimmer

import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import org.simplifide.client.{DisplayElement, Page}
import org.simplifide.swim.{PageUtils, NavBar, WebConstants}
import org.simplifide.swim.model.ModelWrapper.{PersonWithEntryList, PersonWithEntry}
import org.simplifide.swim.model.{ModelWrapper, Entry, PersonWithTimes}
import rx.Rx
import rx.Var

import scalatags.JsDom.all
import scalatags.JsDom.all._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by andyw_000 on 11/24/2015.
  */
class SwimmerPage extends DisplayElement with Page {

  val displayId = "searchPage"


  val swimmer = new Var("")
  val entries = new Var(ModelWrapper.emptyPerson)

  val navBar     = new NavBar
  val menu       = new RaceSelectionPanel
  val baseTable2 = new SwimmerTable2(entries,menu.state)
  val plot       = new SwimmerPlotPanel (entries, baseTable2.code)

  override def createHtmlElement: all.HtmlTag = {
    menu.attach(WebConstants.sideBar1)
    plot.attach(WebConstants.botColunm)
    navBar.attach(WebConstants.navBarDiv)

    div()
  }
  val reg  = s"""/swimmer/(.*)""".r

  override def create: Unit = {
    baseTable2.create
    menu.create
    dom.document.location.pathname match {
      case reg(x) => swimmer.update(x)
      case _      => dom.console.error("Couldn't Decode Location")
    }

  }


  Rx {
      val res = Ajax.post("entry", swimmer())

      res.map(x => {
        val newEntries = upickle.default.read[PersonWithEntryList](x.responseText)
        entries.update(newEntries)
        PageUtils.changeTitle(newEntries.person.name.displayString + " Times")
        PageUtils.changePageTitle(newEntries.person.name.displayString + " Times")

      })
    }




}
