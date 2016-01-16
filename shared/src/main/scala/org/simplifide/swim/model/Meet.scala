package org.simplifide.swim.model


/**
 * Created by andy.wagner on 11/3/2015.
 */

// TODO : Need Section for meets page search
case class Meet(_id:String, n:String, d:Long) extends IdHolder{


  val name = n
  val date = d

  val id=s"$name$date".hashCode.toString


}

object Meet {

}

