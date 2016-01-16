package org.simplifide.swim

import org.scalajs.dom
import org.simplifide.client.db.MongoInterface.{DbWrapper}
import rx.core.Obs

/**
  * Created by Andy on 12/11/2015.
  */
object MainDb {
  val db    = new DbWrapper("swimmers", List("teams","people","meets","results"))



  Obs(db.db) {
    dom.console.warn("Storing Data to Db" )
  }

}
