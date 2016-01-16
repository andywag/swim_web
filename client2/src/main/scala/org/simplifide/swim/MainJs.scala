package org.simplifide.swim

import org.scalajs.dom.ext.Ajax
import org.scalajs.jquery.JQueryEventObject
import org.simplifide.swim.model.{Entry, PersonWithTimes}
import org.simplifide.swim.swimmer.SwimmerPage
import rx.core.Var

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scalatags.JsDom.all._

/**
  * Created by Andy on 11/10/2015.
  */
object MainJs extends js.JSApp {

  val title = Var("TopMenu")
  val ec = scala.concurrent.ExecutionContext.Implicits.global




  // Date Set Used for this Operation
  val people                                 = Var(Iterable[PersonWithTimes]())

  //val person:Var[Option[PersonWithTimes]]    = Var(None)
  val entries:Var[Iterable[Entry]]           = Var(Iterable())

  val activePersonPage                       = Var(false)

  //val mainSwimmer  = new MainSwimmerPage



  //val searchWindow = new SwimmerSearchMenu
  //val raceSearch   = new RaceSearchMenu
  //val mainSearch   = new MainSearchPage(people,searchWindow.state, raceSearch.state)
  //val mainSwimmer  = new MainSwimmerPage2(people,searchWindow.state, raceSearch.state)


  ///val swimmerStrok = new SwimmerStrokeTable("swimTest",people,searchWindow.state,raceSearch.state,Rx{true})

  //val searchPage = new SearchPage()

  //val site = new MainSite()

  def main() = {
    val swimPage = new SwimmerPage()
    swimPage.createHtmlElement
    swimPage.create
    //site.createSite()
    //searchPage.createHtmlElement
    //searchPage.create
    /*
    mainSearch.attach(WebConstants.topColumn)
    mainSearch.create

    //mainSwimmer.attach(WebConstants.topColumn)
    //mainSwimmer.create

    searchWindow.attach(WebConstants.sideBar1)
    searchWindow.create

    raceSearch.attach(WebConstants.sideBar2)
    raceSearch.create
  */


    //val nav = new NavBar()
    //nav.attach(WebConstants.navBarDiv)


  }


  def handlePersonClick(e:JQueryEventObject) = {
    val res = Ajax.post("entry",e.delegateTarget.getAttribute("id"))
    res.map(x => {
      val people1 = upickle.default.read[List[Entry]](x.responseText)
      entries.update(people1)
      activePersonPage.update(true)
    })

  }

  def handleTeamClick(e:JQueryEventObject) = {

  }



  def createNavBar = {
    val di = ul(`class`:="nav navbar-nav navbar-right")(
      li("Swimmers"),
      li("Teams"),
      li("Meets")
    )
   // dom.document.getElementById(WebConstants.topNavBar).appendChild(di.render)
    di
  }

  /** Load Based on Query Changes*/
  /*
  Rx {
    val res = Ajax.post("post",QueryConverter.queryToJson(searchWindow.query()),0, Map("Content-type"->"application/json"))
    res.map(x => {
      val people1 = upickle.default.read[List[PersonWithTimes]](x.responseText)
      people.update(people1)
    })
  }

  val gender = Rx {searchWindow.genderMenu.selection().value}
  val age = Rx {searchWindow.ageMenu.selection}
  */





}
