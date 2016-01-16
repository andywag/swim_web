package org.simplifide.client

import scalatags.JsDom.all._

/**
  * Created by andy.wagner on 12/31/2015.
  */
trait SimpleElement extends DisplayElement {
  val displayId:String = ""
  //val displayId:String
  def createHtmlElement:HtmlTag
  def create:Unit = {}
}

object SimpleElement {
  object Break extends SimpleElement {
    def createHtmlElement:HtmlTag = br()
  }

  class FieldSet(leg:String, fields:List[DisplayElement]) extends SimpleElement {
    override def create = {
      fields.foreach(_.create)
    }
    def createHtmlElement:HtmlTag = fieldset(
      legend(leg),
      fields.map(x => x.createHtmlElement)
    )
  }
}
