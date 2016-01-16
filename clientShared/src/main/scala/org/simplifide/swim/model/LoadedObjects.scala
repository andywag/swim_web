package org.simplifide.swim.model

import org.scalajs.dom.ext.{SessionStorage, LocalStorage, Ajax}
import org.simplifide.swim.PowerPoint.PolyTable
import org.simplifide.swim.SwimmingSections.Sections
import org.simplifide.swim.{PowerPoint, TimeMeasureTable}
import rx.core.Var
import rx.ops.AsyncRxOps

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Andy on 12/15/2015.
  */
object LoadedObjects {

  val storage = SessionStorage

  /** Load the external object and store if it doesn't exist */
  def loadedObject(address:String, local:String) = {
    System.out.println("Loading Address")
    def loadExternal = Ajax.get(address).map(x => {
      val res = x.responseText
      storage.update(local,res)
      res
    })
    storage(local).map(x => Future(x)).getOrElse(loadExternal)
  }

  object Session {
    private val localStorage = "sectionMenuStorage"
    private val location     = "/assets/sections.json"

    private val loadedMeets = LoadedObjects.loadedObject(location,localStorage)
    private def create(x:String) = upickle.default.read[Sections](x)

    val sections = new AsyncRxOps (Var {
      loadedMeets.map(x => {
        val res = create(x)
        res
      })
    }).async(storage(localStorage).map(x => create(x)).getOrElse(Sections(List())))
  }

  object Power {
    private val localStorage = "powerPoints"
    private val location     = "/assets/poly.json"

    private val loadedMeets = LoadedObjects.loadedObject(location,localStorage)
    private def create(x:String) = upickle.default.read[PolyTable](x)

    val points = new AsyncRxOps (Var {
      loadedMeets.map(x => {
        val res = create(x)
        res
      })
    }).async(storage(localStorage).map(x => create(x)).getOrElse(PowerPoint.emptyTable))
  }

  object Times {
    val timeId      = "times"
    val loadedTimes = LoadedObjects.loadedObject("/assets/times.json",timeId)

    def createTable(x:String) = {
      val res = upickle.default.read[TimeMeasureTable](x)
      res
    }
    val times = new AsyncRxOps (Var {
      loadedTimes.map(x => {
        val res = createTable(x)
        res
      })
    }).async(storage(timeId).map(createTable(_)).getOrElse(TimeMeasureTable(List())))

  }


}
