package controllers

import org.simplifide.swim.SwimmingSections.Sections
import play.api.Play
import play.api.Play.current

/**
  * Created by Andy on 1/3/2016.
  */
object LocalAsset {

  def get(location:String) = {
    val url = Play.classloader.getResourceAsStream(location)
    val input = scala.io.Source.fromInputStream(url)
    input.mkString
  }

  lazy val sections = {
    val sectionText = get("public/sections.json")
    upickle.default.read[Sections](sectionText)
  }

  /*
  val times = {
    val sectionText = get("public/times.json")
    upickle.default.read[Sections](sectionText)
  }
  */

}
