package org.simplifide.swim.model

import java.util.{Calendar, Date}

import org.simplifide.swim.model.Person.Gender
/**
 * Created by andy.wagner on 10/29/2015.
 */
case class Person(_id:String, n:Person.Name, b:Double, g:Int,
                  t:String="", r:Double=0.0, s:Option[String] = None) {

  val name = n
  val bday = b
  val gender = Gender(g)
  val teamId = t
  val rating = r
  val shortCode = None

}

object Person {

  val C_ID      = "_id"
  val C_NAME    = "n"
  val C_BIRTH   = "b"
  val C_TEAM_ID = "t"
  val C_GENDER  = "g"
  val C_CODE    = "s"
  val C_RATING  = "r"
  //val C_TIMES   = "times"


  sealed case class Gender(value:Int) {
    override def toString = if (value == 0) "M" else if (value == 1) "F" else "Both"
  }
  object Both extends Gender(2)
  object Male extends Gender(0)
  object Female extends Gender(1)

  case class Name(f:String, l:String, i:Option[String])  {

    val first   = f
    val last    = l
    val initial = i

    override def toString = first + " " + last
    def displayString = first.capitalize + " " + last.capitalize

    def createId = {
      //first.split(" ")(0) + last.split(" ")(0)
      createUsaId._2
    }
    /** Creates the USA Id as well as one without the middle iniitial. The middle initial causes ambiguity  */
    def createUsaId = {
      val fi = first.length match {
        case 0 => "***"
        case 1 => first.substring(0,1) + "**"
        case 2 => first.substring(0,2) + "*"
        case _ => first.substring(0,3)
      }
      val mi = initial.getOrElse("*")
      val li = last.length match {
        case 0 => "****"
        case 1 => last.substring(0,1) + "***"
        case 2 => last.substring(0,2) + "**"
        case 3 => last.substring(0,3) + "*"
        case _ => last.substring(0,4)
      }
      ((fi + mi + li).toUpperCase, (fi + li).toUpperCase)
    }
  }

  object Name {
    val C_FIRST   = "first"
    val C_LAST    = "last"
    val C_INITIAL = "initial"
  }





}
