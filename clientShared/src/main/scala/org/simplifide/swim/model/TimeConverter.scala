package org.simplifide.swim.model

import java.util.concurrent.TimeUnit

import org.scalajs.dom
import org.scalajs.dom.ext.{LocalStorage, Ajax}
import org.simplifide.swim.SwimmingSections.Sections
import org.simplifide.swim.model.Person.Gender
import org.simplifide.swim.{WebConstants, PowerPointConverter, TimeMeasureTable}
import org.simplifide.swim.TimeMeasureTable.{Standard, N}
import org.simplifide.swim.model.ModelWrapper.PersonWithEntry
import rx.core.{Var, Rx}
import rx.ops.AsyncRxOps

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scalatags.JsDom.all._
import scalatags.generic.Attr

import scala.concurrent.ExecutionContext.Implicits.global

object TimeConverter {

  // Load up the times values from the loaded object section
  val times  = LoadedObjects.Times.times
  val points = LoadedObjects.Power.points

  val power = new PowerPointConverter {
    val personToAge:(Person)=>Int = x => Utils.toAge(x.bday)
    def getPoints(age:Int,code:Code,gender:Gender,time:Double):Int = {
      points().checkTime(age,code,gender,time)
    }
  }

  /**
    * Convenience class to hold the display values for a time display table
  */
  case class TimeReturn(timeString:String,
                        meetString:String,
                        realTime:Double,
                        time:Standard,
                        point:Int,
                        code:Code) {

    def realTimeString(typ:Int) = {
      val tim = Utils.timeString(realTime)
      tim.map(x => {
        typ match {
          case WebConstants.DISPLAY_NONE =>  s"$x"
          case WebConstants.DISPLAY_POINT => s"$x($point)"
          case WebConstants.DISPLAY_TIME =>  s"$x($time)"
          case _                            => ""
        }
      }).getOrElse("NT")
    }
  }



  def timeStandardByPersonEntry(code:Code, person:Person,entry:Option[Entry]) = {
    val age  = Utils.toAge(person.bday)

    val point = entry.map(y => {
      y.d.r.typ match {
        case Time.NORM => points().checkTime(age, y.r.c, person.gender, y.d.r.v)
        case _         => 0
      }
    }).getOrElse(0)



    val time = entry.map(y => {
      times().checkTime(age, y.r.c, person.gender, y.d.r.v)
    }).getOrElse(N)

    // TODO : Meet Broken - Looking through old meet
    val res = TimeReturn(entry.map(_.d.r.toString + s"($time)").getOrElse("NT"),
      entry.map(x => Utils.meetOut(x)).getOrElse(""),
      entry.map(_.d.r.v).getOrElse(500.0),
      time,
      point,
      code)



    res
}

  /** Method Called by the code/entry */
  def getTimeStandardNew(code:Code, x:PersonWithEntry) = {
    val entry = x.entries.get(code)
    timeStandardByPersonEntry(code, x.person,entry)
  }

  def pointsByPersonEntry(person:Person,entry:Option[Entry]):Int = {
    /*val age  = Utils.toAge(person.bday)
    entry.map(y => {
      y.detail.result.typ match {
        case Time.NORM => points().checkTime(age, y.race.code, person.gender, y.detail.result.value)
        case _         => 0
      }
    }).getOrElse(0)*/
    power.pointsByPersonEntry(person,entry)
  }

  /*** Method Called by the code/entry */
  def getEntryforImx(code:Code, x:PersonWithEntry, imx:Boolean) = {
    // TODO : Need to Handle LCM/SCY
    // TODO : Need Extra Column for 13+ for Extra IM
    power.getEntryforImx(code,x,imx)
  }

  /*** Method Called by the code/entry */
  def getTimeStandardImx(code:Code, x:PersonWithEntry, imx:Boolean) = {
    val entry = getEntryforImx(code:Code, x:PersonWithEntry, imx:Boolean)
    val res = timeStandardByPersonEntry(code,x.person,entry)

    res
  }

  def getTotalImx(x:PersonWithEntry, imx:Boolean) = {
    power.getTotalImx(x,imx)
  }

  def getAvgImx(x:PersonWithEntry, imx:Boolean) = {
    power.getAvgImx(x,imx)
  }


  /** Create the Display tag */
  def createDisplayTag(times:TimeReturn, displayType:Int) = {
    val tip = times.meetString
    val text = times.realTimeString(displayType)
    //val text = times.timeString
    val order = times.realTime
    val code = times.code.codeString

    val aa = div(href:="#",Attr("data_toggle"):="tooltip",
      Attr("title"):=tip)(text)

    td(`class`:=s"time${times.time.toString}", Attr("code"):=code, Attr("data-order"):=order)(aa)
  }

  /** Create the Total Tag */


}
