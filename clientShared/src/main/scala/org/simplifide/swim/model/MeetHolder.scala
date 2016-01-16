package org.simplifide.swim.model

import org.scalajs.dom.ext.{SessionStorage, LocalStorage, Ajax}
import rx.core.Var
import rx.ops.AsyncRxOps

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by andy.wagner on 12/15/2015.
  */
object MeetHolder {

  val meetId = "meets"

  def createMap(x:String) = {
    try {
      val res = upickle.default.read[List[Meet]](x).map(y => (y._id,y)).toMap
      res
    }
    catch  {
      case _ => Map[String,Meet]()
    }

  }

  // Create a better way to handle the meet information
  // Currently just loads all the meets

  val loadedMeets = LoadedObjects.loadedObject("/meets",meetId)
  val meets = new AsyncRxOps (Var {
    loadedMeets.map(x => {
      val res = createMap(x)
      res
    })
  }).async(createMap(SessionStorage(meetId).getOrElse("")))

  def apply(id:String) = {
    val res = meets().get(id)
    res
  }



}
