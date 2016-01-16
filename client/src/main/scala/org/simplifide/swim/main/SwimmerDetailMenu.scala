package org.simplifide.swim.main

import org.scalajs.dom
import org.simplifide.client.DisplayElement
import org.simplifide.client.widgets.SelectMenu.{DefaultMenuItem, MenuItem}
import org.simplifide.client.widgets.{SelectMenu}
import org.simplifide.swim.main.SwimmerDetailMenu.State
import org.simplifide.swim.model.Person
import rx.core.{Rx, Var}


import scalatags.JsDom.all
import scalatags.JsDom.all._

/**
  * Created by andy.wagner on 11/12/2015.
  */
class SwimmerDetailMenu(parent:DisplayElement)  {

  val displayId = "swimmerDetailMenu"

  import SwimmerDetailMenu._
  val genderMenu  = new SelectMenu(parent.createSubSection("Gender"),  C_GENDER, SwimmerDetailMenu.genders)
  val ageMenu     = new SelectMenu(parent.createSubSection("Age"),     C_AGE,    SwimmerDetailMenu.ages)

  val state = new State(Rx(ageMenu.selection().value), Rx(genderMenu.selection().value))

}

object SwimmerDetailMenu {

  val C_GENDER  = "Gender"
  val C_AGE     = "Age"

  //case class GenderMenuItem(value:Int, display:String) extends MenuItem

  object All    extends DefaultMenuItem("All"    ,Person.Both.value    ,0)
  object Female extends DefaultMenuItem("Female" ,Person.Female.value  ,1)
  object Male   extends DefaultMenuItem("Male"   ,Person.Male.value    ,2)

  val genders = List(All, Female, Male)

  //case class AgeMenuItem(value:Int, display:String) extends MenuItem

  object AllAge extends DefaultMenuItem("All" , 0, 0 )
  object U8     extends DefaultMenuItem("U8"  , 1, 1 )
  object U10    extends DefaultMenuItem("U10" , 2, 2 )
  object U12    extends DefaultMenuItem("U12", 3, 3 )
  object U14    extends DefaultMenuItem("U14", 4, 4 )
  object U16    extends DefaultMenuItem("U16", 5, 5 )
  object U18    extends DefaultMenuItem("U18", 6, 6 )
  object O18    extends DefaultMenuItem("O18" , 7, 7 )

  val ages = List(AllAge,U8,U10,U12,U14,U16,U18,O18)


  case class State(age:Rx[Int], gender:Rx[Int])


}


