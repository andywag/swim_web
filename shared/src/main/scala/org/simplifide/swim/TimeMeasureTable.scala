package org.simplifide.swim

import org.simplifide.swim.TimeMeasureTable._
import org.simplifide.swim.model.Person.Gender
import org.simplifide.swim.model.{Person, Entry, Code}

/**
  * Created by andy.wagner on 11/30/2015.
  */
case class TimeMeasureTable(entries:List[TimeStandard]) {
  //def checkTime(result:)

  def checkUnder8(code:Code, time:Double) = {
    val result = (code) match {
      case Code(1,25,0) => if (time < 19.49) A else if (time < 23.49) B else N
      case Code(2,25,0) => if (time < 23.49) A else if (time < 28.49) B else N
      case Code(3,25,0) => if (time < 25.99) A else if (time < 31.49) B else N
      case Code(4,25,0) => if (time < 22.59) A else if (time < 27.39) B else N
      case Code(1,50,0) => if (time < 43.99) A else if (time < 53.29) B else N
      case Code(2,50,0) => if (time < 51.99) A else if (time < 62.99) B else N
      case Code(3,50,0) => if (time < 57.99) A else if (time < 70.19) B else N
      case Code(4,50,0) => if (time < 54.99) A else if (time < 66.59) B else N
      case Code(1,50,1) => if (time < 49.99) A else if (time < 60.49) B else N
      case Code(2,50,1) => if (time < 58.99) A else if (time < 71.39) B else N
      case Code(3,50,1) => if (time < 63.99) A else if (time < 77.49) B else N
      case Code(4,50,1) => if (time < 63.99) A else if (time < 77.49) B else N

      case Code(1,100,0) => if (time < 60+39.99) A else if (time < 120.99)    B else N
      case Code(1,100,1) => if (time < 60+52.99) A else if (time < 120+16.79) B else N
      case Code(5,100,0) => if (time < 60+49.29) A else if (time < 120+12.29) B else N
      case _ => N
    }
    if (time < 10.0) N else result
  }

  def getTimeStandard(age:Int,code:Code,sex:Gender) = {
    def roundAge(age:Int) = {
      if (age <= 10) 10
      else if (age <= 12) 12
      else if (age <= 14) 14
      else if (age <= 16) 16
      else if (age <= 18) 18
      else age
    }

    val res2  = entries.filter(x => x.gender == sex.value)
    val res1  = res2.filter(x => (x.age == roundAge(age)))
    val res   = res1.filter(x => (x.code == code))
    if (res.length > 0) Some(res(0)) else None

  }


  // TODO : Need to Put A/B Times to Plot
  // TODO : Might Want to Put 8A,8B Instead to Distinguish
  def checkTime(age:Int, code:Code, sex:Gender, time:Double) = {

    val res  = getTimeStandard(age:Int,code:Code,sex:Gender)
    val resa = res.map(x => {
      if      (time < 10) N
      else if (time < x.times(5)) AAAA
      else if (time < x.times(4)) AAA
      else if (time < x.times(3)) AA
      else if (time < x.times(2)) A
      else if (time < x.times(1)) BB
      else if (time < x.times(0)) B
      else C
    }).getOrElse(N)

    val res2 = if (age <= 8) checkUnder8(code,time) else resa
    res2
  }
}

object TimeMeasureTable {
  case class TimeStandard(age:Int, gender:Int, code:Code, times:List[Double]) {
    override def toString = {
      s"L - $age - $gender - $code - $times"
    }
  }

  sealed class Standard(val value : String)
  case object AAAA extends Standard("AAAA")
  case object AAA  extends Standard("AAA")
  case object AA   extends Standard("AA")
  case object A    extends Standard("A")
  case object BB   extends Standard("BB")
  case object B    extends Standard("B")
  case object C    extends Standard("C")
  case object N    extends Standard("N")




  val base = "C:\\coursera\\swim_web\\client\\src\\main\\resources\\times.xml"

}
