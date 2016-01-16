package org.simplifide.swim.model

import org.simplifide.swim.model.Time.{NORM, TimeType}

// TODO : Convert Value from Double to Int 1000*msec
// TODO : Remove Measure (Redundant Information) - Used in Parser so need extra storage there
// TODO : Remove Time Type Don't Need to Know invalid time (If So Encode in Int)

/**
  * @param v    : Actual Time stored as a double
  * @param t      : Type of Time Should be stored as Int or Not at All
  * @param m  : Measure
  */
case class Time(v:Double, t:Int, m:Int = Time.SCY)  {


  val typ = TimeType(t)

  lazy val realV = typ match {
    case Time.NORM => v
    case _         => 10000.0
  }


  override def toString = {
    def createTime(d:Double) = Time.timeString(d)

    typ match {
      case Time.NORM => createTime(v)
      case Time.SCR  => "SCR"
      case Time.DQ   => "DQ"
      case Time.NT   => "NT"
      case Time.NS   => "NS"
    }

  }

  def validTime = {
      typ match {
        case Time.NORM => true
        case Time.DQ   => true
        case _         => false
      }
  }

  def displayTime = {
    typ match {
      case Time.NORM => true
      case _         => false
    }
  }

  def getFastestTime(time:Time) = if (isInputFaster(time)) time else this

  def isInputFaster(time:Time) = {
    if (time.typ.typ < this.typ.typ) true
    else if (time.typ.typ > this.typ.typ) false
    else (time.v < this.v)

  }

}

object Time {
  case class TimeType(val typ:Int)
  object NORM extends TimeType(0)
  object SCR  extends TimeType(1)
  object DQ   extends TimeType(2)
  object NT   extends TimeType(3)
  object NS   extends TimeType(4)

  def apply(value:Double) = new Time(value,NORM.typ)
  val NOTIME = new Time(1.0,NT.typ)

  //case class Measure(val value:Int)
  val SCY   =0
  val LCM   =1
  val SCM   =2
  val NONE  =4

  def timeString(d:Double) = {
    val m = math.floor(d/60.0);
    val r = math.round( (d - m*60.0)*100.0)/100.0
    if (r < 10) s"$m:0${r}" else s"$m:${r}"
  }

}

