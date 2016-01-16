package org.simplifide.swim.model

/**
  *
  * @param _id
  * @param p : Person in database
  * @param b : List of Best Times (Link to Entry Database)
  */
case class PersonWithBest(_id:String, p:Person, b:List[String]) extends IdHolder {
  val person    = p
  val bestTimes = b
}

object PersonWithBest {
  val C_PERSON = "p"
}
