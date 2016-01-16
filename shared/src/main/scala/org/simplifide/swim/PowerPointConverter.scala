package org.simplifide.swim

import org.simplifide.swim.model.Person.Gender
import org.simplifide.swim.model.{Time, Person, Entry, Code}
import org.simplifide.swim.model.ModelWrapper.PersonWithEntry

/**
  * Created by Andy on 12/17/2015.
  */

/**
  * Converter from Person/Code to PowerPoint
  * 1. Funtion to calculate the age of a person
  * 2.
  */
trait PowerPointConverter {

  val personToAge:(Person)=>Int
  def getPoints(age:Int,code:Code,gender:Gender,time:Double):Int

  def pointsByPersonEntry(person:Person,entry:Option[Entry]):Int = {
    val age  = personToAge(person)
    entry.map(y => {
      y.d.r.typ match {
        case Time.NORM => getPoints(age, y.r.c, person.gender, y.d.r.v)
        case _         => 0
      }
    }).getOrElse(0)
  }

  /*** Method Called by the code/entry */
  def getEntryforImx(code:Code, x:PersonWithEntry, imx:Boolean) = {
    // TODO : Need to Handle LCM/SCY
    // TODO : Need Extra Column for 13+ for Extra IM
    val uage  = personToAge(x.person)
    val age   = if (uage <= 10) 0 else if (uage <= 12) 1 else 2
    val newDistance = (age,code.typ) match {
      case (0,1) => if (imx) 200 else 100
      case (0,5) => if (imx) 200 else 100
      case (0,_) => if (imx) 100 else 50
      case (1,1) => if (imx) 500 else 200
      case (1,5) => if (imx) 200 else 100
      case (1,_) => if (imx) 100 else 50
      case (2,1) => if (imx) 500 else 200
      case (2,5) => if (imx) 400 else 200
      case (2,_) => if (imx) 200 else 200
    }
    val newCode = code.copy(l = newDistance)
    val entry = x.entries.get(newCode)
    //if (newCode.typ == 2)
    //  System.out.println(s"Entry : $age $uage $newDistance $code $newCode $entry ${x.entries}")

    entry
  }



  def getTotalImx(x:PersonWithEntry, imx:Boolean) = {
    val results = List.tabulate(5)(y=> getEntryforImx(Code(y+1,50),x,imx))
    val points  = results.map(y => pointsByPersonEntry(x.person,y))
    //System.out.println(results)
    //System.out.println(points)
    results.map(y => pointsByPersonEntry(x.person,y)).foldLeft(0)(_+_)
  }

  def getAvgImx(x:PersonWithEntry, imx:Boolean) = {
    val results = List.tabulate(5)(y=> getEntryforImx(Code(y+1,50),x,imx))
    val value = results.map(y => pointsByPersonEntry(x.person,y)).foldLeft(0)(_+_)
    if (results.flatten.size > 0) value/results.flatten.size else 0
  }

  def getAvgTotalImx(x:PersonWithEntry):Double = {
    if (x.person._id.equalsIgnoreCase("ALEFIRE10383")) {
      val b = 1;
    }
    val results1 = List.tabulate(5)(y=> getEntryforImx(Code(y+1,50),x,true))
    val results2 = List.tabulate(5)(y=> getEntryforImx(Code(y+1,50),x,false))
    val results = results1 ::: results2

    val total = results.flatten.size
    val value = results.map(y => pointsByPersonEntry(x.person,y)).foldLeft(0)(_+_)
    if (total > 2) value.toDouble/total.toDouble else 0.0
  }


}
