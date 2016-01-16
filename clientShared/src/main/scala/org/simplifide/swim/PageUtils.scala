package org.simplifide.swim

import org.scalajs.dom

/**
  * Created by andy.wagner on 1/5/2016.
  */
object PageUtils {

  private def updateText(id:String, text:String) = {
    org.scalajs.jquery.jQuery(s"#$id").text(text)
  }

  def changeTitle(title:String) = {
    updateText(WebConstants.mainTitle,s"SwimStats : $title")
  }

  def changePageTitle(title:String) = {
    updateText(WebConstants.pageTitle,title)
  }

}
