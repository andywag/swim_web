package org.simplifide.swim.model

/**
  * Created by andy.wagner on 11/11/2015.
  */
case class Code(t:Int, l:Int, m:Int=Time.SCY) extends Ordered[Code] {

  val typ = t
  val length = l
  val measure = m

  def codeString = s"$length$typ$measure"
  def c = measure match {
    case Time.LCM => "M"
    case _        => "Y"
  }

  def displayString = {
    def rtyp = typ match {
      case 1 => "Free"
      case 2 => "Back"
      case 3 => "Breast"
      case 4 => "Fly"
      case 5 => "Im"
      case _ => "??"
    }
    s"$length$c $rtyp"
  }

  /*
  override def compare(x: Code, y: Code): Int = {
    val r0 = x.length.compareTo(y.length)
    val r1 = if (r0 == 0) x.typ.compareTo(y.typ) else r0
    val r2 = if (r1 == 0) x.measure.compareTo(y.measure) else r1
    r2
  }
  */
  import scala.math.Ordered.orderingToOrdered
  override def compare(that: Code): Int = {
    (this.length,this.typ,this.measure) compare (that.length,that.typ,that.measure)
  }
}

object Code {
  val C_FREE   = 1
  val C_BACK   = 2
  val C_BREAST = 3
  val C_FLY    = 4
  val C_IM     = 5

  def decodeCode(value:Int) = {
    val measure   = value % 10;
    val race      = (value-measure)/10 % 10;
    val distance  = (value - race*10 - measure)/100;

    Code(race,distance,measure)
  }

  class CodeType(val r:String, val index:Int)
  case object S_FREE   extends CodeType("Free",C_FREE)
  case object S_BACK   extends CodeType("Back",C_BACK)
  case object S_BREAST extends CodeType("Breast",C_BREAST)
  case object S_FLY    extends CodeType("Fly",C_FLY)
  case object S_IM     extends CodeType("IM",C_IM)

  val S_RACES = List(S_FREE, S_BACK, S_BREAST, S_FLY, S_IM)

  case class Distance(length:Int) {
    override def toString = length.toString
  }
  val S_DISTANCES = {
    val d = List(25,50,100,200,400,500,1000,1500)
    d.map(x => new Distance(x))
  }

  class CodeMeasure(val r:String, val ind:Int)
  case object SCY extends CodeMeasure("SCY",Time.SCY)
  case object LCM extends CodeMeasure("LCM",Time.LCM)
  case object SCM extends CodeMeasure("SCM",Time.SCM)

  val S_MEASURES = List(SCY, LCM, SCM)

}
