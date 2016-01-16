package org.simplifide.swim.model

/**
  * Created by andy.wagner on 11/18/2015.
  */
case class BestTime(items:List[BestTime.Item]) {

  /** Compare results with new results and return a new value with the fastest times */
   def combine(time:BestTime):BestTime = {
    /** Retrieve the best times out of a list of times (should be 2 items) */
     def reduce(items:List[BestTime.Item]) = {
      val res = if (items.length == 1) items(0) else items(0).fastest(items(1))
      res
    }
     val total = items ++ time.items
     val res   = total.groupBy(_.code)
     val comb  = res.map(x => reduce(x._2))
     new BestTime(comb.toList)
   }

  def getTime(code:Code):Option[BestTime.Item] = {
    val res = items.filter(x => x.code == code)
    if (res.length == 0) None else Some(res(0))
  }

}

object BestTime {

  val C_ITEMS  = "items"

  val C_CODE   = "code"
  val C_MEET   = "meet"
  val C_TIME   = "time"

  case class Item(code:Code, meet:Meet, detail:Detail, race:Race) {
    def fastestTime = if (detail.r.isInputFaster(detail.f)) detail.r else detail.f
    def toolTipText = s"${meet.name} on ${meet.date}"

    def fastest(item:Item):Item = if (detail.r.isInputFaster(item.detail.r)) item else this
  }

}
