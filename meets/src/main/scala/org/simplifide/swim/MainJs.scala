package org.simplifide.swim

import org.simplifide.swim.meets.MeetPage

import scala.scalajs.js

/**
  * Created by Andy on 1/9/2016.
  */
object MainJs extends js.JSApp{

  def main() = {
    val mainPage = new MeetPage()
    mainPage.createHtmlElement
    mainPage.create
  }
}
