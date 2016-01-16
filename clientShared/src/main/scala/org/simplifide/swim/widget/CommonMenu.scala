package org.simplifide.swim.widget

import org.simplifide.client.widgets.SelectMenu.DefaultMenuItem
import org.simplifide.swim.model.Person

/**
  * Created by andy.wagner on 1/13/2016.
  */
object CommonMenu {
  case class MenuDetails(id:String,title:String,items:List[DefaultMenuItem])

  val C_GENDER  = "Gender"
  val C_AGE     = "Age"

  object All    extends DefaultMenuItem("All"    ,Person.Both.value    ,0)
  object Female extends DefaultMenuItem("Female" ,Person.Female.value  ,1)
  object Male   extends DefaultMenuItem("Male"   ,Person.Male.value    ,2)
  val menuGender = MenuDetails(C_GENDER, C_GENDER,List(All, Female, Male))


  object AllAge extends DefaultMenuItem("All" , 0, 0 )
  object U8     extends DefaultMenuItem("U8"  , 1, 1 )
  object U10    extends DefaultMenuItem("U10" , 2, 2 )
  object U12    extends DefaultMenuItem("1112", 3, 3 )
  object U14    extends DefaultMenuItem("1314", 4, 4 )
  object U16    extends DefaultMenuItem("1516", 5, 5 )
  object U18    extends DefaultMenuItem("U18",  6, 6 )
  object O18    extends DefaultMenuItem("O18" , 7, 7 )
 //val menuAge = List(AllAge,U8,U10,U12,U14,U16,U18,O18)
  val menuAge = MenuDetails(C_AGE, C_AGE, List(AllAge,U8,U10,U12,U14,U16,U18,O18))


  val S_DISTANCES = {
    val d = List(25,50,100,200,400,500,1000,1500)
    DefaultMenuItem("All",0,0) :: d.zipWithIndex.map(x => new DefaultMenuItem(x._1.toString,x._1.toInt, x._2))
  }
  val menuDistance = MenuDetails("Distance","Distance",S_DISTANCES)

  val S_STROKE = {
    val d = List("All","Free","Back","Breast","Fly","IM")
    d.zipWithIndex.map(x => new DefaultMenuItem(x._1.toString,x._2, x._2))
  }
  val menuStroke = MenuDetails("Stroke","Stroke",S_STROKE)


}
