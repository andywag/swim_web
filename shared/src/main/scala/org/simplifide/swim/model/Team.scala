package org.simplifide.swim.model

/**
 * Created by andy.wagner on 11/3/2015.
 */
case class Team(_id:String, n:String) extends IdHolder{
  //val base = BSONDocument(ModelConstants.C_ID -> id)
  val base = Map(ModelConstants.C_ID -> _id)

  val name = n
}

object Team {
  val C_NAME    = "id"
  val C_PEOPLE  = "name"

  val default = Team("None","None")

}

