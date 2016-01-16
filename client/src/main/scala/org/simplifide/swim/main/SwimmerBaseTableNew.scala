package org.simplifide.swim.main

import org.scalajs.dom
import org.scalajs.jquery.JQueryEventObject
import org.simplifide.client.datatable.BaseTable
import org.simplifide.query.QueryModel
import org.simplifide.swim.TimeMeasureTable.{Standard, N}
import org.simplifide.swim.model.ModelWrapper.PersonWithEntry
import org.simplifide.swim.model._
import org.simplifide.swim.{TimeMeasureTable, WebConstants, model}
import rx.core.{Obs, Rx}

import scala.concurrent.ExecutionContext
import scalatags.JsDom.all._
import scalatags.generic.Attr
import scala.scalajs.js.JSConverters._


/**
  * Created by andyw_000 on 11/23/2015.
  */
trait SwimmerBaseTableNew extends BaseTable[PersonWithEntry] {

  type R = PersonWithEntry

  val detailState:SwimmerDetailMenu.State
  val raceState:RaceSearchPanel.State
  val updating:Rx[Boolean]

  // TODO : Find a way to deal with loading
  Obs(updating, skipInitial = true) {
    if (updating()) { // If Updating has been set to true updat
      //this.clearTable
    }
  }


  Obs(raceState.display,skipInitial = true) {
    this.redrawTable()
  }


  def code(code:Code)(x:R) = {
    TimeConverter.getTimeStandardNew(code,x)
  }


  val swimmerColumn = new Column {
    val title = "Swimmer"
    //override def display(p:R) = p.person.name.displayString
    override def displayTag(p1:R) = {
      td(a(href:=s"swimmer/${p1.person._id}",id:=p1.person._id,`class`:=WebConstants.A_SWIMMER)(p1.person.name.displayString))
    }
  }
  val teamColumn = new Column {
    val title = "Team"
    //override def display(p:R) = p.person.teamId
    override def displayTag(p1:R) = {
      td(a(href:=s"#team/${p1.person.teamId}",id:=p1.person.teamId,`class`:=WebConstants.A_TEAM)(p1.person.teamId))
    }
  }
  val genderColumn = new Column {
    val title = "Gender"
    override def display(p:R) = p.person.gender.toString
    def filterGender(e:Int)(person:R):Option[R]= {
      e match {
        case 2 => Some(person)
        case _ => if (person.person.gender.value == e) Some(person) else None
      }
    }
    override lazy val filterItem = Rx{filterGender(detailState.gender())_}
  }
  val ageColumn = new Column {
    val title = "Age"
    override def display(p:R) = model.Utils.toAge(p.person.bday)
    def filterAge(e:Int)(person:R)= {
      val x = model.Utils.toAge(person.person.bday)
      val res = e match {
        case QueryModel.U8.value =>  if (x <= 8) Some(person) else None
        case QueryModel.U10.value => if (x <= 10) Some(person) else None
        case QueryModel.U12.value => if (x <= 12) Some(person) else None
        case QueryModel.U14.value => if (x <= 14) Some(person) else None
        case QueryModel.U16.value => if (x <= 16) Some(person) else None
        case QueryModel.U18.value => if (x <= 18) Some(person) else None
        case QueryModel.O18.value => if (x > 18 ) Some(person) else None
        case _ => Some(person)
      }
      res
    }
    override lazy val filterItem = Rx{filterAge(detailState.age())_}
  }

  case class TimeColumn(override val title:String, f:(R)=>TimeConverter.TimeReturn) extends Column {
    override def displayTag(p:R):Tag = {
      val times = f(p)
      TimeConverter.createDisplayTag(times, raceState.display())
    }
  }








}

object SwimmerBaseTableNew {

  case class State(gender: Int = 0, age: Int = 0)

  class SwimmerStrokeTable(override val displayId: String,
                           val rxData: Rx[Iterable[PersonWithEntry]],
                           val detailState: SwimmerDetailMenu.State,
                           val raceState: RaceSearchPanel.State,
                           val active: Rx[Boolean],
                           val updating:Rx[Boolean])(implicit ec: ExecutionContext) extends SwimmerBaseTableNew {

    override val initialOrder = ("5,desc")

    lazy val columns = Rx {

      // TODO : Use this method instead of repeated code below
      def col(tit:String, stroke:Int, measure:Int) =
        new TimeColumn(tit, code(Code(stroke, raceState.distance(),measure)))

      List(swimmerColumn, teamColumn, genderColumn, ageColumn,
        new TimeColumn(s"Free(Y)", code(Code(1, raceState.distance(),0))),
        new TimeColumn(s"Back(Y)", code(Code(2, raceState.distance(),0))),
        new TimeColumn(s"Breast(Y)", code(Code(3, raceState.distance(),0))),
        new TimeColumn(s"Fly(Y)", code(Code(4, raceState.distance(),0))),
        new TimeColumn(s"IM(Y)", code(Code(5, raceState.distance(),0))),
        new TimeColumn(s"Free(M)", code(Code(1, raceState.distance(),1))),
        new TimeColumn(s"Back(M)", code(Code(2, raceState.distance(),1))),
        new TimeColumn(s"Breast(M)", code(Code(3, raceState.distance(),1))),
        new TimeColumn(s"Fly(M)", code(Code(4, raceState.distance(),1))),
        new TimeColumn(s"IM(M)", code(Code(5, raceState.distance(),1))))
    }

    Rx {
      if (active()) {
        raceState.measure() match {
          case Time.SCY => {
            List.tabulate(5)(x => setColumnVisible(5 + x, true))
            List.tabulate(5)(x => setColumnVisible(10 + x, false))
          }
          case Time.LCM => {
            List.tabulate(5)(x => setColumnVisible(5 + x, false))
            List.tabulate(5)(x => setColumnVisible(10 + x, true))
          }
          case _ => {
            List.tabulate(5)(x => setColumnVisible(5 + x, true))
            List.tabulate(5)(x => setColumnVisible(10 + x, true))
          }
        }
      }
    }
  }

  class SwimmerDistanceTable(override val displayId: String,
                             val rxData: Rx[Iterable[PersonWithEntry]],
                             val detailState: SwimmerDetailMenu.State,
                             val raceState: RaceSearchPanel.State,
                             val active: Rx[Boolean],
                             val stroke: Int,
                             val updating:Rx[Boolean])(implicit ec: ExecutionContext) extends SwimmerBaseTableNew {

    override val initialOrder = ("6,desc")

    lazy val columns = Rx {

      def create(distance:Int, measure:Int) = {
        val ex = if (measure == 0) "Y" else "L"
        new TimeColumn(s"$distance$ex", code(Code(stroke, distance,measure)))
      }

      def getColumns(measure:Int):List[Column] = {


        val times = (measure,stroke) match {
          case (Time.SCY,1) => List(25,50,100,200,500,1000,1500)
          case (Time.LCM,1) => List(50,100,200,400,800,1500)
          case (Time.SCY,5) => List(100,200,400)
          case (Time.LCM,5) => List(200,400)
          case (Time.SCY,_) => List(25,50,100,200)
          case (Time.LCM,_) => List(50,100,200)
        }
        times.map(x => create(x,measure))
      }


      val head = List(swimmerColumn, teamColumn, genderColumn, ageColumn)
      val scy  = getColumns(0)

      val lcm  = getColumns(1)
      val res  = head ::: scy ::: lcm
      res
    }

    Rx {

      if (active()) {
        columns().zipWithIndex.foreach(x => {
          val index = x._2 +1
          x._1 match {
            case TimeColumn(t,_) => raceState.measure() match {
              case Time.SCY => if (t.contains("Y")) setColumnVisible(index,true) else setColumnVisible(index,false)
              case Time.LCM => if (t.contains("L")) setColumnVisible(index,true) else setColumnVisible(index,false)
              case _        => setColumnVisible(index,true)
            }
            case _ => // Do Nothing
          }
        })

      }

    }

  }

  class ImxTable(override val displayId: String,
                 val rxData: Rx[Iterable[PersonWithEntry]],
                 val detailState: SwimmerDetailMenu.State,
                 val raceState: RaceSearchPanel.State,
                 val active: Rx[Boolean],
                 stroke: Int,
                 val updating:Rx[Boolean],
                 imx:Boolean = false
                 )(implicit ec: ExecutionContext) extends SwimmerBaseTableNew {

    Obs(raceState.measure, skipInitial = true) {
      this.redrawTable()
    }

    class ImxColumn(override val title:String, f:(R)=>TimeConverter.TimeReturn) extends Column {
      override def displayTag(p:R):Tag = {
        val times = f(p)
        val res = TimeConverter.createDisplayTag(times, raceState.display())

        res
      }
    }

    class TotalColumn extends Column{
      override val title = "Total"
      override def displayTag(p:R):Tag = td(TimeConverter.getTotalImx(p,imx))
    }

    class AverageColumn extends Column{
      override val title = "Avg"
      override def displayTag(p:R):Tag = td(TimeConverter.getAvgImx(p,imx))
    }

    def imxCode(code:Code)(x:R) = {
      TimeConverter.getTimeStandardImx(code,x,imx)
    }

    lazy val columns = Rx {

      val measure = if (raceState.measure() == Time.LCM) Time.LCM else Time.SCY
      List(swimmerColumn, teamColumn, genderColumn, ageColumn,
        new ImxColumn(s"Free",   imxCode(Code(1, 50, measure))),
        new ImxColumn(s"Back",   imxCode(Code(2, 50, measure))),
        new ImxColumn(s"Breast", imxCode(Code(3, 50, measure))),
        new ImxColumn(s"Fly",    imxCode(Code(4, 50, measure))),
        new ImxColumn(s"IM",     imxCode(Code(5, 50, measure))),
          new TotalColumn,
          new AverageColumn
      )
    }
    override val initialOrder = ("10,desc")


  }

}
