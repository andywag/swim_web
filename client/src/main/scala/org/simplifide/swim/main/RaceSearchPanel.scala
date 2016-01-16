package org.simplifide.swim.main

import org.simplifide.client.DisplayElement
import org.simplifide.client.widgets.{SelectPanel, SelectMenu}
import org.simplifide.client.widgets.SelectMenu.{DefaultMenuItem, MenuItem}
import org.simplifide.swim.WebConstants
import org.simplifide.swim.model.{Time, Code}
import rx.core.{Var, Rx}

import scalatags.JsDom.all
import scalatags.JsDom.all._

/**
  * Created by andy.wagner on 11/20/2015.
  */
class RaceSearchPanel extends DisplayElement with SelectPanel {

  import RaceSearchPanel._

  val displayId = "swimmerDetailMenu"

  val distanceMenu = new SelectMenu(createSubSection("Distance"),C_DISTANCE,  S_DISTANCES, 1)
  val measureMenu  = new SelectMenu(createSubSection("Measure"), C_MEASURE,   S_MEASURES, 1)
  val displayMenu  = new SelectMenu(createSubSection("Display"), WebConstants.C_DISPLAY,   WebConstants.S_DISPLAY)

  val menus = List(distanceMenu,measureMenu,displayMenu)
  val state = new State(Rx(distanceMenu.selection().value),
    Rx(measureMenu.selection().value),
    Rx(displayMenu.selection().value))

}

object RaceSearchPanel {
  val C_TYPE     = "Type"
  val C_DISTANCE = "Distance"
  val C_MEASURE  = "Measure"

  val S_DISTANCES = {
    val d = List(25,50,100,200,400,500,1000,1500)
    d.zipWithIndex.map(x => new DefaultMenuItem(x._1.toString,x._1, x._2))
  }

  val SCY = new DefaultMenuItem("SCY", Time.SCY,  0)
  val LCM = new DefaultMenuItem("LCM", Time.LCM , 1)
  val ALL = new DefaultMenuItem("All", 2 , 2)

  val S_MEASURES = List(ALL, SCY, LCM)


  case class State(distance:Rx[Int], measure:Rx[Int], display:Rx[Int])



}
