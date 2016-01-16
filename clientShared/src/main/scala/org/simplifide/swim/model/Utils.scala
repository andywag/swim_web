package org.simplifide.swim.model

import scala.scalajs.js.Date

/**
  * Created by Andy on 11/15/2015.
  */
object Utils {

  def timeString(d:Double) = {
    val m = math.floor(d / 60.0);
    val r = math.round((d - m * 60.0) * 100.0) / 100.0
    val res1 = if (r < 1) None else if (r < 10) Some(s"$m:0${r}") else Some(s"$m:${r}")
    val res  = if (d == 500) None else res1
    res
  }

  def toYear(d:Double) = new Date(d).getFullYear()


  def dateString(d:Double) = {
    val date   = new Date(d)
    s"${date.getMonth()+1}/${date.getDate()}/${date.getFullYear()}"

  }

  def output(date:Double) = {
    val current = new Date()
    val birth   = new Date(date)

    birth.toLocaleDateString()

  }

  def meetOut(entry:Entry) = {

    MeetHolder.apply(entry.m).map {
      x => {
        s"${x.name} on ${output(x.date)}"
      }
    }.getOrElse("")
  }


  def ageOnDate(birthDate:Date, checkDate:Date) = {
    val current = checkDate
    val birth   = birthDate

    (current.getTime() - birth.getTime())

    val age = current.getFullYear() - birth.getFullYear()
    val dm  = if (current.getMonth() < birth.getMonth()) -1 else 0
    val dm2 = if (current.getMonth() == birth.getMonth() && (current.getDate() < birth.getDate())) -1 else 0

    age + dm + dm2
  }

  def toAge(date:Double) = {
    val current = new Date()
    val birth   = new Date(date)
    ageOnDate(birth,current)

    /*
    (current.getTime() - birth.getTime())

    val age = current.getFullYear() - birth.getFullYear()
    val dm  = if (current.getMonth() < birth.getMonth()) -1 else 0
    val dm2 = if (current.getMonth() == birth.getMonth() && (current.getDate() < birth.getDate())) -1 else 0

    age + dm + dm2
    */
  }

}
