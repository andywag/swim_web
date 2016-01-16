package org.simplifide.swim.meets

import org.simplifide.client.SimpleElement.FieldSet
import org.simplifide.client.widgets.SelectMenu.DefaultMenuItem
import org.simplifide.client.widgets.{SelectMenu, RxSelectMenu, SelectPanel}
import org.simplifide.client.{DisplayElement, SimpleElement}
import org.simplifide.swim.meets.CompositeSearchPanel.{State, YearMenu, MeetMenu}
import org.simplifide.swim.model._
import org.simplifide.swim.widget.CommonMenu
import rx.core.{Var, Obs, Rx}

/**
  * Created by andy.wagner on 12/31/2015.
  */
class CompositeSearchPanel extends DisplayElement with SelectPanel {

  override val displayId: String = "meetsSearchPanel"

  val yearMenu = new YearMenu()
  val meetMenu = new MeetMenu(yearMenu.year)

  val genderSelect    = createSelectPanel(CommonMenu.menuGender)
  val ageSelect       = createSelectPanel(CommonMenu.menuAge)
  val strokeSelect    = createSelectPanel(CommonMenu.menuStroke)
  val distanceSelect  = createSelectPanel(CommonMenu.menuDistance)

  val state = State(genderSelect.value,
    ageSelect.value,
    strokeSelect.value,
    distanceSelect.value)

  val menus:List[DisplayElement] = List(
    new FieldSet("Details", List(
      yearMenu.yearSelect,
      CompositeSearchPanel.sectionMenu,
      meetMenu.meetMenu

    )),
    SimpleElement.Break,
    new FieldSet("Filters", List(
      genderSelect,
      ageSelect,
      strokeSelect,
      distanceSelect
    )),
    SimpleElement.Break,
    new FieldSet("Race", List()
  ))

}

object CompositeSearchPanel {
  // Year Search Panel
  class YearMenu {
    val YEARS      = List.tabulate(11)(x => new DefaultMenuItem((2016-x).toString, x,  x))
    val yearSelect = new SelectMenu("yearPanel","Years",YEARS,0)
    val year = Rx {
      2016 - yearSelect.selection().index
    }
  }


  // Section Search Panel
  val sections = LoadedObjects.Session.sections
  val sectionMenuItems = Rx{
    DefaultMenuItem("All",0,0) :: sections().sections.zipWithIndex.map(x => new DefaultMenuItem(x._1.name,x._2+1, x._2+1))
  }
  val sectionMenu = new RxSelectMenu("sectionPanel", "Sections", sectionMenuItems,1 )


  class MeetMenu(year:Rx[Int])  {
    // Meets Search Panel
    val meets = MeetHolder.meets()
    // TODO : Need to Add Filter
    val filteredMeets = Rx {

      val res1 = MeetHolder.meets().map(x => x._2).toList
      val res = res1.filter(x => Utils.toYear(x.d.toDouble) == year())
      res
    }
    val meetItems = Rx {
      def meetString(meet:Meet) = {
        s"${Utils.dateString(meet.d)} : ${meet.n.trim}"
      }
      val res = filteredMeets().sortBy(x => x.d).zipWithIndex.map(x => new DefaultMenuItem(meetString(x._1),x._2+1,x._2+1))
      new DefaultMenuItem("Select",0,0) :: res
    }
    val meetMenu = new RxSelectMenu("meetPanel", "Meets", meetItems,0)

    val selectedMeet:Rx[Option[Meet]] = Rx {
      val index = meetMenu.selection().index
      if (filteredMeets().size > index)
        Some(filteredMeets()(meetMenu.selection().index))
      else
        None
    }
  }

  case class State(gender:Rx[Int], age:Rx[Int], stroke:Rx[Int], distance:Rx[Int]) {
    override def toString = s"${gender()} ${age()} ${stroke()} ${distance()}"
  }





}

