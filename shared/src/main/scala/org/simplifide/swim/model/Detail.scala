package org.simplifide.swim.model

/**
  * Created by andy.wagner on 11/11/2015.
  */
case class Detail(r:Time, f:Time) {
  def getFastestTime = r.getFastestTime(f)
}

object Detail {
}