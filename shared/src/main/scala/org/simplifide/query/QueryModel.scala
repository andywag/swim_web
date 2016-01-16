package org.simplifide.query

/**
  * Created by andy.wagner on 11/16/2015.
  */
case class QueryModel(section:String,
                      zone:String,
                      team:String
                     ) {

  import QueryModel._
  def oSection = if (section.equals("All")) None else Some(section)
  def oZone    = if (zone.equals("All")) None else Some(zone)
  def oTeam    = if (team.equals("All")) None else Some(team)

  def getMap = Map(C_SECTION -> oSection,
    C_ZONE   -> oZone,
    C_TEAM   -> oTeam
  )

}

object QueryModel {

  case class QueryModelInt(section:String,zone:String,team:String)  {
    def toQueryModel = QueryModel(section,zone,team)
  }

  val C_SECTION = "section"
  val C_ZONE    = "zone"
  val C_TEAM    = "team"


  case class Age(value:Int, name:String) {
    override def toString = name
  }
  object All extends Age(0, "All")
  object U8  extends Age(1, "U8")
  object U10 extends Age(2, "U10")
  object U12 extends Age(3, "U12")
  object U14 extends Age(4, "U14")
  object U16 extends Age(5, "U16")
  object U18 extends Age(6, "U18")
  object O18 extends Age(7, "O18")


}
