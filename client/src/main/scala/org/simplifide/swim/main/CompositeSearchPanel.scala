package org.simplifide.swim.main

import org.simplifide.client.SimpleElement.FieldSet
import org.simplifide.client.{SimpleElement, DisplayElement}
import org.simplifide.client.widgets.{SelectPanel, SelectMenu}
import org.simplifide.swim.WebConstants
import org.simplifide.swim.main.{RaceSearchPanel, SwimmerDetailMenu, SwimmerSearchPanel}

import scalatags.JsDom.all

/**
  * Created by andy.wagner on 12/31/2015.
  */
class CompositeSearchPanel extends DisplayElement with SelectPanel {

  override val displayId: String = "compositeSearchPanel"


  val s = new SwimmerSearchPanel
  val d = new SwimmerDetailMenu(this)
  val r = new RaceSearchPanel

  val menus:List[DisplayElement] = List(
    new FieldSet("Location", List(s.sectionMenu,
    s.zoneMenu,
    s.teamMenu)),
    SimpleElement.Break,
    new FieldSet("Person", List(d.genderMenu,
    d.ageMenu)),
    SimpleElement.Break,
    new FieldSet("Race",List(r.distanceMenu,
    r.measureMenu,
    r.displayMenu)
  ))




  //val distanceMenu = new SelectMenu(createSubSection("Distance"),C_DISTANCE,  S_DISTANCES, 1)
  //val measureMenu  = new SelectMenu(createSubSection("Measure"), C_MEASURE,   S_MEASURES)
  //val displayMenu  = new SelectMenu(createSubSection("Display"), WebConstants.C_DISPLAY,   WebConstants.S_DISPLAY)

}
