package org.simplifide.swim.swimmer

import org.scalajs.dom
import org.simplifide.client.{SimpleElement, DisplayElement}
import org.simplifide.client.SimpleElement.FieldSet
import org.simplifide.client.widgets.{SelectPanel, SelectMenu}
import org.simplifide.client.widgets.SelectMenu.DefaultMenuItem
import org.simplifide.query.QueryModel
import org.simplifide.query.QueryModel.Age
import org.simplifide.swim.WebConstants
import org.simplifide.swim.model.{Time, Code}
import org.simplifide.swim.model.Person.Gender
import rx.core.Rx

import scalatags.JsDom.all
import scalatags.JsDom.all._

/**
  * Created by andy.wagner on 11/12/2015.
  */
class RaceSelectionPanel extends DisplayElement with SelectPanel  {

  import RaceSelectionPanel._

  val displayId = "swimmerMenu"

  val measureMenu   = new SelectMenu(createSubSection("Measure"), C_MEASURE,   measures)
  val distanceMenu  = new SelectMenu(createSubSection("Distance"),C_DISTANCE,  distances)
  val strokeMenu   = new SelectMenu(createSubSection("Stroke"),C_STROKE,strokes)
  val displayMenu  = new SelectMenu(createSubSection("Display"), WebConstants.C_DISPLAY,  WebConstants. S_DISPLAY)


  val state   = new State(Rx(measureMenu.selection().value),
    Rx(distanceMenu.selection().value),
    Rx(strokeMenu.selection().value),
    Rx(displayMenu.selection().value))

  val menus:List[DisplayElement] = List(
    new FieldSet("Details", List(measureMenu, distanceMenu, strokeMenu, displayMenu)),
    SimpleElement.Break)





}

object RaceSelectionPanel {
  val C_MEASURE  = "Measure"
  val C_DISTANCE = "Distance"
  val C_STROKE   = "Stroke"

  val MEASURE_ALL = new DefaultMenuItem("All",Time.NONE,0)
  val MEASURE_SCY = new DefaultMenuItem("SCY",Time.SCY,1)
  val MEASURE_LCM = new DefaultMenuItem("LCM",Time.LCM,2)

  val STROKE_ALL     =  new DefaultMenuItem("All"    ,0              ,0)
  val STROKE_FREE    =  new DefaultMenuItem("Free"   ,Code.C_FREE    ,1)
  val STROKE_BACK    =  new DefaultMenuItem("Back"   ,Code.C_BACK    ,2)
  val STROKE_BREAST  =  new DefaultMenuItem("Breast" ,Code.C_BREAST  ,3)
  val STROKE_FLY     =  new DefaultMenuItem("Fly"    ,Code.C_FLY     ,4)
  val STROKE_IM      =  new DefaultMenuItem("IM"     ,Code.C_IM      ,5)

  val dist = List(0,25,50,100,200,400,500,1000,1500)
  val DIST = {
    def createItem(x:(Int,Int)) = if ((x._1) == 0) new DefaultMenuItem("All",0,0)
    else  new DefaultMenuItem(x._1.toString,x._1,x._2)
    dist.zipWithIndex.map(x => createItem(x))
  }

  val measures = List(MEASURE_ALL, MEASURE_SCY, MEASURE_LCM)
  val strokes = List(STROKE_ALL,STROKE_FREE,STROKE_BACK,STROKE_BREAST,STROKE_FLY,STROKE_IM)
  val distances = DIST




  case class State(measure:Rx[Int], distance:Rx[Int], stroke:Rx[Int], display:Rx[Int])

}
