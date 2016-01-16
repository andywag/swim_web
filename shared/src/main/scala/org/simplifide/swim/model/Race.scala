package org.simplifide.swim.model

/**
  * Created by andy.wagner on 11/11/2015.
  */

// TODO : Refactor the race into a database describing the meet
//        which is correspondingly looked up through an index
// TODO : Convert the date to an int (fractional time like minutes not needed )
/**
  *
  * @param d : Date of Event
  * @param e : Number of the Event
  * @param n   Description of the Event
  * @param c : Race Code
  */
case class Race(d:Double, e:String, n:String, c:Code)  {

  val nDecode = """(\d*)(.*)""".r

  lazy val gender = e match {
    case nDecode(d,t) => {
      if (d.toInt % 2 == 0) Person.Male.value else Person.Female.value
    }
    case _ => Person.Male.value
  }

  lazy val eNumber = e match {
    case nDecode(d,t) => {
      d.length match {
        case 1 => s"00$d$t"
        case 2 => s"0$d$t"
        case 3 => s"$d$t"
        case _ => e
      }
    }
    case _ => e
  }
}

object Race {

}
