package org.simplifide.swim.model

/**
  *
  * @param _id : ID of Class
  * @param m   : Meet (Index to Meet Database)
  * @param p   : Person (index to Person Database)
  * @param d   : Pointer to Detail Section
  * @param r   : Pointer to Race Section
  */
case class Entry(_id:String, m:String, p:String, d:Detail, r:Race  ) extends IdHolder {



  /** Returns the faster entry */
  def getFasterEntry(entry:Entry) = {
    if (d.getFastestTime.isInputFaster(entry.d.getFastestTime)) entry else this
  }
}

object Entry {

  val C_PERSON = "p"
  val C_MEET   = "m"

  /** Return the best times from this list */
  def getBestTimes(entries:List[Entry]) = {
    def getBestFromGroup(groups:List[Entry]) = {
      groups.reduceLeft((x,y)=>x.getFasterEntry(y))
    }
    val groups = entries.groupBy(x => x.r.c) // Group the entries by code
    groups.map(x => getBestFromGroup(x._2))
  }
}
