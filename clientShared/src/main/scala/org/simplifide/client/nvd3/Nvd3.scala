package org.simplifide.client.nvd3

import org.scalajs.jquery.JQueryStatic
import org.simplifide.swim.SwimmingSections.Model

import scala.scalajs.js
import scala.scalajs.js.Any
import scala.scalajs.js.annotation.JSName

/**
  * Created by andyw_000 on 12/19/2015.
  */

trait Axis extends js.Object {
  def axisLabel(in:js.Any):Axis  = js.native
  def tickFormat(in:js.Any):Axis = js.native
  def scale():Axis                 = js.native
  def domain(in:js.Any):Axis     = js.native
}

trait Chart extends js.Function{
  def showLegend(in:js.Any):Chart              = js.native
  def showYAxis(in:js.Any):Chart               = js.native
  def showXAxis(in:js.Any):Chart               = js.native
  def useInteractiveGuideline(in:js.Any):Chart = js.native
  val xAxis:Axis      = js.native
  val yAxis:Axis      = js.native
  def update():Chart    = js.native
}

trait Models extends js.Object{
  def lineChart():Chart = js.native
}



@JSName("nv")
@js.native
object nv extends js.Object {
  def addGraph(inputs:js.Any*) = js.native
  val models:Models = js.native

}
