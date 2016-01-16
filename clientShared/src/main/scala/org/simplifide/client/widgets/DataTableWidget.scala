package org.simplifide.client.widgets

import org.simplifide.client.DisplayElement
import org.simplifide.client.datatable.{InternalTable, DataTable}

import scala.scalajs.js

/**
  * Created by andy.wagner on 12/2/2015.
  */
trait DataTableWidget {
  self:DisplayElement =>


  // Store the Order of the Table
  val orderIndex =  createSubSection("Order")
  val options:js.Dictionary[AnyVal]
  lazy val internalTable = DataTable.jQuery(s"#$displayId").DataTable(options)
  def currentTable          = org.scalajs.jquery.jQuery(s"#$displayId").asInstanceOf[InternalTable]
   /*
  def createTable = {
    val id = s"#$displayId"
    System.out.println("Creating Table " + id)
    DataTable.jQuery(id).DataTable(options)
  }
  */

}
