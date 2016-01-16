package org.simplifide.client.datatable

import org.scalajs.jquery.{JQuery, JQueryStatic}

import scala.scalajs.js

/**
  * Created by andy.wagner on 11/13/2015.
  */
/*
trait DataTableInternal extends js.Object {
  def fnClearTable():DataTableInternal = js.native
}
*/

trait InternalTableAdd extends js.Object {
  def add(inputs:js.Any*)
  val test = js.native
}

trait TablesColumns extends js.Object {
  def adjust():TablesColumns = js.native
}

trait TablesData extends js.Object {
  val columns:TablesColumns = js.native
  def adjust():TablesData = js.native
}

trait TableVisible extends js.Object {
  def visible(input:js.Any*) = js.native
  def nodes():js.Array[org.scalajs.dom.Element] = js.native
  def cells():js.Array[js.Object] = js.native

}

trait InternalTable extends js.Object with JQuery {
  def clear():InternalTable = js.native
  val row:InternalTableAdd = js.native
  def draw():InternalTableAdd = js.native
  def column(input:js.Any*):TableVisible = js.native
  //def data():InternalTable = js.native
  def filter(input:js.Any*):InternalTable = js.native
  def order(input:js.Any*):InternalTable  = js.native
  def tables(input:js.Any*):TablesData = js.native
  val columns:TablesData = js.native
  val tables:TablesData = js.native
  def settings():InternalTable = js.native
  def order():InternalTable = js.native
  //def fnDeleteRow(input:js.Any):InternalTable = js.native
}

trait JQueryPimped extends JQuery {
  def DataTable():InternalTable = js.native
  def DataTable(inputs:js.Any*):InternalTable = js.native
  def clear():InternalTable = js.native
  def draw():InternalTableAdd = js.native
  def selectpicker(input:js.Any*):JQueryPimped = js.native
}


object DataTable extends js.GlobalScope {
  trait JQueryStaticPimped extends JQueryStatic {
    override def apply(name:String):JQueryPimped = js.native
  }

  val jQuery: JQueryStaticPimped = js.native
}
