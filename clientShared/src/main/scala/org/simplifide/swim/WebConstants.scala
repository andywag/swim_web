package org.simplifide.swim

import org.simplifide.client.widgets.SelectMenu.DefaultMenuItem

/**
  * Created by Andy on 11/10/2015.
  */
object WebConstants {

  val pageTitle = "pageTitle"
  val mainTitle = "mainTitle"

  val topNavBar = "topNavBar"
  val navBarDiv = "navBarDiv"


  val sideBar1  = "sideBar1"
  val sideBar2  = "sideBar2"
  val sideBar3  = "sideBar3"

  val topColumn = "topColumn"
  val botColunm = "botColumn"


  val sideBar1Button  = "sideBar1Button"
  val sideBar1Swimmer = ("sideBar1Swimmer","Swimmers")
  val sideBar1Meet    = ("sideBar1Meet"   ,"Meets")
  val sideBar1Team    = ("sideBar1Team"   ,"Teams")

  val A_SWIMMER = "swimmerType"
  val A_TEAM    = "teamType"

  // Constants for Display Search Menu
  val DISPLAY_TIME  = 0
  val DISPLAY_POINT = 1
  val DISPLAY_NONE  = 2

  // TODO : Attach WebConstants to Menu Values
  object TimeItem     extends DefaultMenuItem("Time"   ,0  ,0)
  object PointItem    extends DefaultMenuItem("Point"  ,1  ,1)
  object NoItem       extends DefaultMenuItem("None"   ,2  ,2)

  val C_DISPLAY  = "Display"
  val S_DISPLAY = List(TimeItem,PointItem,NoItem)


}
