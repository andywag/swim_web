package org.simplifide.swim.model

import org.simplifide.swim.model.ModelWrapper.PersonWithEntry
import org.simplifide.swim.model.Person.Name

/**
  * Created by andy.wagner on 12/4/2015.
  */
case class ModelWrapper(teams:Map[String,Team] = Map(),
                        meets:Map[String,Meet] = Map(),
                        people:Map[String,PersonWithEntry] = Map(),
                        results:Map[String,Entry] = Map()) {



}

case class ModelReturn(teams:List[Team] = List(),
                       meets:List[Meet] = List(),
                       people:List[PersonWithBest] = List(),
                       results:List[Entry] = List()) {

  val newResults = results.map(x => (x._id,x)).toMap
  val newPeople  = people.map(x => {
    val res = x.bestTimes.map(y => newResults.get(y)).flatten.map(y => (y.r.c,y)).toMap
    (x.person,res)
  }).map(x => PersonWithEntry(x._1, x._2)).map(x => (x.person._id,x)).toMap


  def toModelReturn = ModelWrapper(
    teams.map(x => (x._id,x)).toMap,
    meets.map(x => (x._id, x)).toMap,
    newPeople,
    results.map(x => (x._id, x)).toMap)


}

object ModelWrapper {

  case class PersonWithEntry(person:Person, entries:Map[Code,Entry])
  val EmptyPersonWithEntry = PersonWithEntry(Person("",Name("","",None),0,Person.Male.value),Map())

  case class PersonWithEntryList(person:Person, entries:List[Entry])
  val emptyPerson = PersonWithEntryList(Person("",Name("","",None),0,Person.Male.value),List())


}
