package org.simplifide.swim.meets


import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import org.simplifide.client.{Page, DisplayElement}
import org.simplifide.query.QueryModel
import org.simplifide.swim.model.{ModelWrapper, MeetHolder, ModelReturn}
import org.simplifide.swim.{WebConstants, NavBar}
import rx.core.{Rx, Var, Obs}

import scala.concurrent.ExecutionContext.Implicits.global
import scalatags.JsDom.all
import scalatags.JsDom.all._

/**
  * Created by andyw_000 on 11/24/2015.
  */



class MeetPage extends DisplayElement with Page {

  val displayId = "meetPage"

  val model           = Var[ModelWrapper](ModelWrapper())

  val groups          = Rx {
    val results = model().results.map(x => x._2).toList
    val gr1 = results.groupBy(x => x.r.eNumber)
    val gr = gr1.map(x => (x._1,x._2.sortBy(x => x.d.getFastestTime.realV))).toMap
    gr
  }

  val people          = Rx {model().people}
  val teams           = Rx {model().teams}

  val composite       = new CompositeSearchPanel
  val searchTab       = new MeetTable2(groups, people, teams, composite.state)
  val navBar          = new NavBar

  override def createHtmlElement: all.HtmlTag = {

    System.out.println("Creating Page")
    composite.attach(WebConstants.sideBar1)
    navBar.attach(WebConstants.navBarDiv)
    //searchTab.attach(WebConstants.topColumn)

    div()
  }

  override def create: Unit = {
    composite.create
  }

  Obs(composite.meetMenu.selectedMeet) {
    val meetId = composite.meetMenu.selectedMeet().map(x => {
      val intId = x._id
      val res1   = Ajax.get(s"meetEntry/$intId")
      res1.map(y => {
        val modelReturn = upickle.default.read[ModelReturn](y.responseText)

        dom.console.warn(modelReturn.results.size + " " + modelReturn.people.size + " " + modelReturn.teams.size)
        model.update(modelReturn.toModelReturn)
      })
    })
  }

}
