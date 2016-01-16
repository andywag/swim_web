package org.simplifide.swim

import org.simplifide.swim.model.Code
import org.simplifide.swim.model.Person.Gender

/**
  * Created by andy.wagner on 12/8/2015.
  */
object PowerPoint {

  case class TableDescriptor(age:Int, gender:Int, distance:Int, event:Int, measure:Int)
  case class TableEntry(descriptor:TableDescriptor, items:List[(Double,Int)])
  case class Table(m:Map[TableDescriptor,TableEntry])

  case class PolyEntry(descriptor: TableDescriptor, items:List[Double])
  // TODO : Need poly table to be limited by max/min for times
  case class PolyTable(m:Map[TableDescriptor,PolyEntry]) {
    def checkTime(age:Int, code:Code, sex:Gender, time:Double):Int = {
      //val uage2 = 2*math.ceil(age/2)
      val uage2  = age
      val uage  = if (uage2 >= 16) 16 else if (uage2 < 9) 9 else uage2
      // TODO : Sex is Inverted Between Table Creator and File
      val desc = new TableDescriptor(uage.toInt,sex.value ^ 1,code.length,code.typ,code.measure)
      val poly = m.get(desc)
      val ret = poly.map(x => {
        val taps = x.items.slice(0,x.items.length-1)
        val max  = x.items(x.items.length-1)

        val terms =taps.reverse.zipWithIndex.map(y => y._1*math.pow(time,y._2))
        val total = terms.reduce(_+_)
        val base  = math.max(0.0,math.round(total).toInt).toInt
        //System.out.println("Base " + base)

        if (time > max) 0 else base

      })
      val fin = ret.getOrElse(0)
      //System.out.println(s"$uage $code $sex $time -- $fin")
      fin
    }
  }

  val emptyTable = PolyTable(Map())


  def main(args: Array[String]) {


  }

}
