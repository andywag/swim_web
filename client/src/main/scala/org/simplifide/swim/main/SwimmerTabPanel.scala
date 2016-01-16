package org.simplifide.swim.main

import org.scalajs.dom
import org.simplifide.client.DisplayElement
import org.simplifide.swim.model.{ModelWrapper, PersonWithTimes}
import org.simplifide.swim.WebConstants
import rx.core.{Rx, Var}

import scalatags.JsDom.all
import scalatags.JsDom.all._
import scalatags.generic.Attr
import org.scalajs.jquery.jQuery

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by andyw_000 on 11/21/2015.
  */
class SwimmerTabPanel(modelWrapper:Rx[ModelWrapper],
                      detailState:SwimmerDetailMenu.State,
                      raceState:RaceSearchPanel.State,
                      updating:Rx[Boolean]) extends DisplayElement {

  val ec = scala.concurrent.ExecutionContext.Implicits.global

  val displayId = "swimmerTab"


  val numberOfRows = 8
  val C_BASE = "tabTable"
  val C_SEL  = "tabSelect"

  val tabNames = List("All","Free","Back","Breast","Fly", "IM", "IMR", "IMX")

  val activeTab = Var(6)
  val active = List.tabulate(numberOfRows)(x => Var(if (x==activeTab()) true else false))

  // Initially Load the Table with the IMR Section



  val state = Var(SwimmerBaseTableNew.State())

  val data = Rx {
    modelWrapper().people.map(x => x._2)
  }

  val tabs = {
    def cr(x:Int) =
      if (x == 0) new SwimmerBaseTableNew.SwimmerStrokeTable(s"swimStroke$x"   ,data, detailState,  raceState, active(x),updating)
      else if (x < 6)  new SwimmerBaseTableNew.SwimmerDistanceTable(s"swimStroke$x" ,data,  detailState, raceState, active(x),x,updating)
      else if (x == 7) new SwimmerBaseTableNew.ImxTable(s"IMR" ,data,  detailState, raceState, active(x),x,updating, true)
      else new SwimmerBaseTableNew.ImxTable(s"IMX" ,data,  detailState, raceState, active(x),x,updating)

    List.tabulate(numberOfRows)(x => cr(x))
  }



  def tabId(index:Int) = s"$C_SEL$index"

  override def createHtmlElement: all.HtmlTag = {

    def createTabMenu(index:Int)= {
      val base = if (index == activeTab()) li(`class` := "active") else li
      base(a(href := s"#$C_BASE$index", id := s"$C_SEL$index", Attr("data-toggle") := "tab")(s"${tabNames(index)}"))
    }
    def createTab(index:Int)= {
      val base = if (index == activeTab()) div(`class` := "tab-pane active", id := s"$C_BASE$index") else div(`class` := "tab-pane", id := s"$C_BASE$index")
      base(tabs(index).createHtmlElement)
    }
    div(
      ul(`class` := "nav nav-tabs", role := "tablist")(
        List.tabulate(numberOfRows)(x => createTabMenu(x))
      ),
      div(`class` := "tab-content")(
        List.tabulate(numberOfRows)(x => createTab(x))
      )
    )
  }


  def openTab(index:Int)(): Unit = {
    active(activeTab()).update(false)
    active(index).update(true)
    activeTab.update(index)
    tabs(index).internalTable.columns.adjust()
  }

  def reorderTab(index:Int)():Unit = {
    tabs(index).internalTable.columns.adjust()
  }

  override def create: Unit = {
    val tabSelect = """a[data-toggle="tab"]"""
    tabs.foreach(x => x.create)
    List.tabulate(numberOfRows)(x => jQuery(s"#$C_SEL$x").click(openTab(x)_))
    List.tabulate(numberOfRows)(x => jQuery(tabSelect).on("shown.bs.tab",reorderTab(x)_))

  }



}
