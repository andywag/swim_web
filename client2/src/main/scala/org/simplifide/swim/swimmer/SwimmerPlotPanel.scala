package org.simplifide.swim.swimmer

import org.scalajs.dom
import org.simplifide.client.DisplayElement
import org.simplifide.client.nvd3.LinePlot.Line
import org.simplifide.client.nvd3.{LinePlot, nv}
import org.simplifide.client.widgets.PlotPanel
import org.simplifide.swim.WebConstants
import org.simplifide.swim.model._
import org.simplifide.swim.model.ModelWrapper.PersonWithEntryList
import org.singlespaced.d3js.d3
import rx._
import rx.core.Rx

import scala.scalajs.js
import scala.scalajs.js.Date
import scalatags.JsDom.all._
import org.scalajs.jquery.jQuery
import scala.scalajs.js.JSConverters._


/**
  * Created by andy.wagner on 12/3/2015.
  */
class SwimmerPlotPanel(val baseEntries:Rx[PersonWithEntryList], val code:Rx[Int]) extends DisplayElement with LinePlot{
  val displayId = "swimPlotPanel"
  val svgLocation:String = WebConstants.botColunm


  def timescale(d:Double) = {
    d3.time.format("%m-%d-%y")(new Date(d))
  }

  def timeString(d:Double) = {
    Time.timeString(d)
  }





  def createStandardLine(code:Code, person:Person, dates:List[Date]) = {
    def createColor(x:(Int,Int,Int),scale:Int) = {
      def hex(x:Int) = {
        val res = Integer.toHexString(x)
        res.substring(res.length-2)
      }
      s"#${hex(x._1/scale)}${hex(x._2/scale)}${hex(x._3/scale)}"
    }
    val labels = List("B","BB","A","AA","AAA","AAAA")
    //val base   = (219,186,153)
    //val colors = List.tabulate(6)(x => createColor(base,x+1))
    def createLine(index:Int,values:List[(Date,Double)]) = {
      val li = Line(labels(index),values.map(x => (x._1.getTime(),x._2)),classed=Some("dashed"),opacity=Some(.1))
      li
    }
    val ages = dates.map(x => Utils.ageOnDate(new Date(person.bday),x))
    val res  = (dates zip ages).map(x => TimeConverter.times().getTimeStandard(x._2,code,person.gender).map(y => (x._1,y.times)))
    val fres  = res.flatten
    val total = fres.map(x => x._2.map(y => (x._1,y)))
    val trans = total.transpose
    trans.zipWithIndex.map(x => createLine(x._2,x._1))

  }

  def dates(code:Code, entries1:List[Entry]) = {
    val entries = entries1.filter(x => x.d.getFastestTime.displayTime)  // Remove DQ's From Plot
    entries.map(x => {
      val meet = MeetHolder(x.m).map(x => x.date)
      meet.map(y => (y,x))
    }).flatten.sortBy(x => x._1)
  }
  /** Creates a line based on the code and entries */
  def createLine(code:Code,entries:List[Entry]) = {
    val res1 = dates(code,entries)

    val li = res1.map(x => (x._1.toDouble, x._2.d.getFastestTime.v))
    val li2 = Line(code.displayString,li,width=Some(2))
    li2
  }

  Rx {

    // Find the selected code from the table mouseover event
    val ucode = if (code() == 0) None else Some(Code.decodeCode(code()))

    val codes = baseEntries().entries.map(x => x.r.c).toSet.toList
    val first = ucode.getOrElse(codes(0))

    val ent   = baseEntries().entries.filter(x => x.r.c == first)
    val line  = createLine(first,ent)

    val fastest = 1.1*ent.map(x => x.d.getFastestTime.v).max
    val slowest = .9*ent.map(x => x.d.getFastestTime.v).min



    val newDates = dates(first,ent).map(x => new Date(x._1.toDouble))
    val standard = createStandardLine(first,baseEntries().person,newDates)

    drawLines(line :: standard)

    chart.xAxis.axisLabel("date").tickFormat(timescale _)
    chart.yAxis.axisLabel("time").tickFormat(timeString _)
    // TODO : Limit the range of the plots to only show the
    //chart.yAxis.scale().domain(Array(slowest,fastest).toJSArray)
    dom.console.warn("Limiting to Range " + fastest + " " + slowest)

  }


  def createHtmlElement:HtmlTag = {


      System.out.println(nv)
      val names = scalajs.js.Object.getOwnPropertyNames(nv.asInstanceOf[scalajs.js.Object])





    //dd.data(dd2).call(chart)



    div()
  }

  def create:Unit = {

  }
}
