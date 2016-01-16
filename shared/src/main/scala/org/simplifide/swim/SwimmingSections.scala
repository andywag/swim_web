package org.simplifide.swim




/**
  * Created by Andy on 11/29/2015.
  */
object SwimmingSections {
  sealed trait Model
  case class Team(id:String, description:String, loc:String)    extends Model
  case class Zone(name:String, teams:List[Team])    extends Model
  case class Section(name:String, zones:List[Zone]) extends Model
  case class Sections(sections:List[Section])


}
