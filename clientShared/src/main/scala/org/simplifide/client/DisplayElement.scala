package org.simplifide.client

/**
  * Created by andyw_000 on 11/22/2015.
  */

import org.scalajs.dom
import org.scalajs.dom.Element

import scalatags.JsDom.all._

trait DisplayElement {

  val displayId:String
  def createSubSection(id:String) = s"$displayId$id"
  //val displayId:String
  def createHtmlElement:HtmlTag
  def create:Unit


  def getLocation(location:String):Option[Element] = {
    val res = dom.document.getElementById(location)
    if (res == null) None else Some(res)
  }

  def redraw(location:String) = {
    clear(location)
    attach(location)
  }

  /** Clear the children from this node */
  def clear(location:String) = {
    val loc = getLocation(location)
    loc.map(x => {
      val nodelist = x.childNodes
      for (i <- 0 until nodelist.length) {
        x.removeChild(nodelist.item(0))
      }
    })
  }

  /** Create and Attach this element to this location */
  def attach(location:String) = {
    val loc = getLocation(location)
    loc.map(x => {
      x.appendChild(createHtmlElement.render)
    })

  }



}

object DisplayElement {


}
