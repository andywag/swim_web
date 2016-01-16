package controllers

import org.simplifide.query.QueryModel
import org.simplifide.query.QueryModel.{QueryModelInt}
import org.simplifide.swim.model.Person.{Gender, Name}
import org.simplifide.swim.mongo._
import org.simplifide.swim.utils.FileUtils
import play.api.libs.json.{Reads, Json}
import play.api.mvc._
import play.modules.reactivemongo.json.ImplicitBSONHandlers.{JsObjectWriter, JsObjectReader}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.libs.json._
import play.api.Play.current


import scala.io.Source

// JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax
import play.api.{Play, Logger}

object Application extends Controller {



  // Main Page for Swimmers
  def boot = Action {
    Ok(views.html.newmain("client",List()))
  }

  // Main Page for Meets
  def meetSummary = Action {
    Ok(views.html.newmain("meets",List()))
  }

  // Main Page for Swimmer
  def swim(id:String) = Action.async {
    MongoEntry.getCodes(id).map(x => {
      Ok(views.html.newmain("client2",(x)))
      //Ok(views.html.swimmer2((x)))
    })
  }



  /** Method which resturns a list of all the meets */
  def meets = Action.async {
    MongoMeet.allMeets.map(
      x => {
        val result = upickle.default.write(x)
        Ok(result)
      }
    )
  }




  def times = Action {
    val time = Source.fromFile("times.json").getLines().mkString("\n")
    Ok(time)
  }



  def team(name:String) = Action.async {
   MongoTeam.getTeam(name).map(x => Ok(Json.arr(x.map(y => JsObjectReader.read(y)))))
  }

  def swimmer(team:Option[String], gender:Option[Int], age:Option[Int]) = Action.async {

    MongoPerson.getTeam(team.get).map(x => Ok(Json.arr(x.map(y => JsObjectReader.read(y)))))
  }



  implicit val queryRead:Reads[QueryModelInt] = (
    (JsPath \ QueryModel.C_SECTION).read[String] and
      (JsPath \ QueryModel.C_ZONE).read[String] and
      (JsPath \ QueryModel.C_TEAM).read[String]
    )(QueryModelInt.apply _)


  /** Retrieve the entries based on the query criteria*/
  def post2 = Action.async(parse.json) { request =>
    val res = request.body.validate[QueryModelInt] match {
      case s: JsSuccess[QueryModelInt] => {
        MongoSearch.entriesFromSwimmerPage(s.get.toQueryModel,LocalAsset.sections).map(x => {
          Ok(x)
        })
      }
      case e: JsError => Future(Ok("Failed"))
    }
    res
  }

  /** Get a list of entries based on the person in the request */
  def entry = Action.async(parse.text) { request =>
    MongoEntry.getPersonJson(request.body).map(x => {
      Ok(x)
    })
  }

  /** Return a list of entries based on the meet */
  def entryByMeet(id:String) = Action.async {
    val ret = MongoSearch.entriesFromMeetPage(id)

    ret.map(x => {
      Ok(x)
    })

  }


}
