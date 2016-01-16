package org.simplifide.client.db

import org.scalajs.dom
import org.scalajs.dom.raw._
import rx.core.{Rx, Var}
import rx.ops._

import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  *
  * Created by Andy on 12/11/2015.
  */
class MongoInterface(name:String) {



}

object MongoInterface {


  class DbWrapper(name:String, stores:List[String]) {
    val db = Rx {
      MongoInterface.createDatabase(name,stores, 2).future
    }.async(None)

    def updateStore(store:String, pair:(String,String)) = {
      db().map(x => {
        val trans = x.transaction(store,"write")
        trans.objectStore(store).add(pair._2, pair._1)
      })
    }

    def readStore(store:String, key:String) = {
      db().map(x => {
        val trans = x.transaction(store,"read")
        trans.objectStore(store).get(key)
      })
    }

  }








  def createDatabase(name:String, stores:List[String], version:Int):Promise[Option[IDBDatabase]] = {
    val promise = Promise[Option[IDBDatabase]]()
    val db = dom.window.indexedDB.open(name,version)

    db.onsuccess = (e: Event) => {
      val db = e.target.asInstanceOf[IDBRequest].result.asInstanceOf[IDBDatabase]
      dom.console.warn("Created Db")
      promise.success(Some(db))
    }
    // Updates or creates the data store as needed
    db.onupgradeneeded = (e:Event) => {
      dom.console.warn("Updating DB")
      val db        = e.target.asInstanceOf[IDBRequest].result.asInstanceOf[IDBDatabase]
      val newStores = stores.map(x => db.createObjectStore(x))
      //promise.success(Some(db))
    }
    db.onerror = (e: ErrorEvent) => {
      promise.failure(new IllegalStateException(e.message))
    }
    promise
  }


}
