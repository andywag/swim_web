package org.simplifide.swim.model

/**
  * Created by andy.wagner on 11/19/2015.
  */
case class PersonWithTimes(val _id:String, val person:Person, val times:BestTime)

object PersonWithTimes {

  val C_PERSON = "person"
  val C_TIMES  = "times"

  def apply(person:Person, times:BestTime) = new PersonWithTimes(person._id,person,times)
}
