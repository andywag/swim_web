package org.simplifide.swim.main

import java.io.{File, StringReader}
import java.util.concurrent.TimeUnit

import org.scalajs.dom
import org.scalajs.dom.ext.{SessionStorage, LocalStorage, Ajax}
import org.simplifide.client.widgets.SelectMenu.{DefaultMenuItem, MenuItem}
import org.simplifide.client.{Storable, DisplayElement}
import org.simplifide.client.widgets.{SelectPanel, RxSelectMenu, SelectMenu}
import org.simplifide.query.QueryModel
import org.simplifide.swim.SwimmingSections.{Team, Zone, Section, Sections}
import org.simplifide.swim.TimeMeasureTable
import org.simplifide.swim.model.{LoadedObjects, ModelReturn}
import org.simplifide.swim.model.Person.Gender
import rx.core.{Reactor, Obs, Var, Rx}
import rx.ops.AsyncRxOps

import scala.concurrent.{Future, Await}
import scala.concurrent.duration.Duration
import scalatags.JsDom.all
import scalatags.JsDom.all._
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Created by andy.wagner on 11/12/2015.
  */

// TODO : Clean up this class
class SwimmerSearchPanel extends DisplayElement with SelectPanel  {

  import SwimmerSearchPanel._

  val displayId = "teamSearchMenu"

  val sectionId = "Section"
  val zoneId    = "Zone"
  val teamId    = "Team"

  val sections = LoadedObjects.Session.sections

  // TODO : First Load of Sections doesn't work : Need to either wait for the laod or update the rx select panel

  /** Create the Section Values */
  val sectionMenuItems = Rx{
    DefaultMenuItem("All",0,0) :: sections().sections.zipWithIndex.map(x => new DefaultMenuItem(x._1.name,x._2+1, x._2+1))
  }
  val sectionMenu = new RxSelectMenu(createSubSection(sectionId), C_SECTION, sectionMenuItems,1 )

  val section:Rx[Option[Section]] = Rx {
    val index = sectionMenu.selection().index
    if (index == 0) None else Some(sections().sections(index-1))
  }
  /** End the Section */

  /** Create the Zone Values */
  val zoneMenuItems:Rx[List[DefaultMenuItem]] = Rx {
    val all = new DefaultMenuItem("All",0,0)
    section().map(x => {
      all :: x.zones.zipWithIndex.map(y => new DefaultMenuItem(y._1.name,y._2+1,y._2+1))
    }).getOrElse(List(all))
  }

  val zoneMenu    = new RxSelectMenu(createSubSection(zoneId),    C_ZONE,    zoneMenuItems)

  val zone:Rx[Option[Zone]]     = Rx {
    val index = zoneMenu.selection().index
    if (index == 0) None else section().map(x => x.zones(index-1))
  }

  /** If the Section Changes Clear the Zone */
  Obs(sectionMenu.selection, skipInitial = true) {
    zoneMenu.clearSelection
  }

  /** End Zone Section */

  /** Create the Team Sections */
  val teamMenuItems = Rx {
    val all = new DefaultMenuItem("All",0,0)
    zone().map(x => {
      all :: x.teams.zipWithIndex.map(y => new DefaultMenuItem("PC"+y._1.id,y._2+1,y._2+1))
    }).getOrElse(List(all))
  }
  val teamMenu    = new RxSelectMenu(createSubSection(teamId),    C_TEAM,    teamMenuItems)

  val team:Rx[Option[Team]]     = Rx {
    val index = teamMenu.selection().index
    if (index == 0) None else zone().map(x => x.teams(index-1))
  }

  /** If the Zone Changes Clear the team */
  Obs(zoneMenu.selection, skipInitial = true) {
    teamMenu.clearSelection
  }
  /** End Team Section */

  val menus = List(sectionMenu,zoneMenu,teamMenu)

  val state = State(Rx{teamMenu.selection().display})


  val query = Rx {
    QueryModel(sectionMenu.selection().display,
      zone    =zoneMenu.selection().display,
      team    =teamMenu.selection().display
    )
  }




}

object SwimmerSearchPanel {
  val C_SECTION = "Section"
  val C_ZONE    = "Zone"
  val C_TEAM    = "Team"

  val C_STORAGE = "sectionMenuStorage"


  // TODO : Make sure this is actually being loaded after page laod
  lazy val loadSections = {
    val text =  Ajax.get("/assets/sections.json")
    val res = text.map(x => {upickle.default.read[Sections](x.responseText)})
    text.map(x => {
      SessionStorage.update(C_STORAGE,x.responseText)
    })
    res
  }








  case class State(team:Rx[String])

}
