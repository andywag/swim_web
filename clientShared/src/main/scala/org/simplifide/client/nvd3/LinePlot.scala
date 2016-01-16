package org.simplifide.client.nvd3

import org.simplifide.client.DisplayElement
import org.singlespaced.d3js.d3
import rx.core.{Obs, Rx}

import scala.scalajs.js.JSConverters._


/**
  * Created by andy.wagner on 12/22/2015.
  */
trait LinePlot {
  self:DisplayElement =>

  val svgLocation:String
  val lines:Rx[List[LinePlot.Line[_,_]]] = Rx {List()}

  lazy val chart  = nv.models.lineChart().showLegend(true).showXAxis(true).showYAxis(true).useInteractiveGuideline(true)  //We want nice looking tooltips and a guideline!
  lazy val select = d3.select(s"#${svgLocation} svg")

  def drawLines(lines:Iterable[LinePlot.Line[_,_]]) = {
    val newLines = lines.map(_.create).toJSArray
    select.datum(newLines).call(chart)
  }

  Obs(lines) {

  }

}

object LinePlot {
  case class Line[S,T](key:String, values:List[(S,T)],
                       width:Option[Int] = None, classed:Option[String] = None,
                       color:Option[String] = None,
                       opacity:Option[Double] = None
                      ) {

    def create = {
      def item(x:(S,T)) = Map("x"->x._1, "y"->x._2).toJSDictionary
      val ivalues = values.map(x => item(x)).toJSArray
      val newMap = Map("values"->Some(ivalues),
        "key"->Some(key),
        "strokeWidth"   -> width,
        "classed"       -> classed,
        "color"         -> color,
        "strokeOpacity" -> opacity
      )
      val filteredMap = newMap.filter(x => x._2.isDefined).map(x => (x._1,x._2.get))
      //val res = Map("values"->ivalues, "key"->key).toJSDictionary
      //res
      filteredMap.toJSDictionary
    }
  }
}


