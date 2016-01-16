package org.simplifide.swim

import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import org.simplifide.swim.main.{SwimmerSearchPanel, SearchPage}
import org.simplifide.swim.model.{TimeConverter, Entry, PersonWithTimes}
import rx.core.{Rx, Obs, Var}
import scala.scalajs.js
import scala.scalajs.js.JSON
import scalatags.JsDom.all._
import scala.concurrent.ExecutionContext.Implicits.global


import org.scalajs.jquery.{JQueryEventObject, jQuery}

/**
  * Created by Andy on 11/10/2015.
  */
object MainJs extends js.JSApp {

  val title = Var("TopMenu")
  val ec = scala.concurrent.ExecutionContext.Implicits.global

  // Initialize the times value before running
  TimeConverter.times


  // Date Set Used for this Operation
  val people                                 = Var(Iterable[PersonWithTimes]())

  //val person:Var[Option[PersonWithTimes]]    = Var(None)
  val entries:Var[Iterable[Entry]]           = Var(Iterable())

  val activePersonPage                       = Var(false)


  def main() = {
    val mainPage = new SearchPage()
    mainPage.createHtmlElement
    mainPage.create
  }



}
