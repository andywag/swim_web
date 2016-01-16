package org.simplifide.client

import org.scalajs.dom.ext.SessionStorage

/**
  * Created by Andy on 11/28/2015.
  */
trait Storable {
  type SELF
  def store(id:String)
  def retreve(id:String)


}
