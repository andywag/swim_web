package org.simplifide.swim.main

import org.scalajs.dom
import org.scalajs.dom.ext.{SessionStorage, Ajax}
import org.simplifide.client.db.MongoInterface
import org.simplifide.client.{DisplayElement, Page}
import org.simplifide.query.QueryModel
import org.simplifide.swim.main.CompositeSearchPanel
import org.simplifide.swim.model.{ModelWrapper, ModelReturn, PersonWithBest, PersonWithTimes}
import org.simplifide.swim.{NavBar, MainDb, WebConstants}
import rx.core.{Obs, Rx, Var}

import scalatags.JsDom.all
import scalatags.JsDom.all._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by andyw_000 on 11/24/2015.
  */

// TODO : Speed Up Ordering on Search Change
// TODO : Make Order Change Automatic on Tab Change
// TODO : Keep Search Persistance with Tab Switch
// TODO : Fix Rank Row to Display Correct Rank

class SearchPage extends DisplayElement with Page {

  val displayId = "searchPage"

  val people                                 = Var(Iterable[PersonWithTimes]())

  val modelWrapper  = Var(ModelWrapper())
  val updating:Var[Boolean] = Var(false)

  val composite       = new CompositeSearchPanel
  val searchTab       = new SwimmerTabPanel(modelWrapper,composite.d.state, composite.r.state, updating)
  val navBar          = new NavBar



  override def createHtmlElement: all.HtmlTag = {

    composite.attach(WebConstants.sideBar1)
    searchTab.attach(WebConstants.topColumn)
    navBar.attach(WebConstants.navBarDiv)



    div()
  }

  override def create: Unit = {
    composite.create
    searchTab.create
  }

  /** Load Based on Query Changes - Main Load Function for this Page*/
  Obs(composite.s.query) {
    val write = upickle.default.write[QueryModel](composite.s.query())
    updating.update(true)
    dom.console.warn("Posting Write")
    dom.console.warn(write)
     val res1 = Ajax.post("post2",write,0, Map("Content-type"->"application/json"))
      res1.map(x => {
        val people1 = upickle.default.read[ModelReturn](x.responseText)
        dom.console.warn(people1.people.size)
        updating.update(false)
        modelWrapper.update(people1.toModelReturn)
      })

    }
  //}
}
