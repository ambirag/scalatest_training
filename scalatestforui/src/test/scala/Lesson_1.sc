import org.scalatest.Assertions._
import org.scalatest._

//Lesson 1 : Jan 31st 2017


val y = 5
var z = 5
val x: Double = 5


def f(x: Int) :Int = { x*x }
f(2)

(1 to 5).map(2*_)
(1 to 5).map { x => val y=x*2; println(y); y }
(1 to 5).map( x => x*x )
val str : Array[String]= Array("one, two, three")
str.foreach {
 x =>  x.map {y => Seq(y);println(y);}
}
val (a, b, c, d) = (2, 2, 3, 4)

object HelloWorld {
  def main(args: Array[String]) {
    println("Hello, world!")
  }
}

val xValues = 1 to 4
val yValues = 1 to 2
val coordinates = for {
  x <- xValues
  y <- yValues
} yield (x, y)
coordinates(4)

val ca = 3
val da = 3
assertResult(2,"test failed due to result not being 2") { ca + da }
//Lesson 1 about Specs (feature spec)
class TVSet {
  private var on: Boolean = false
  def isOn: Boolean = on
  def pressPowerButton() { on = !on }
}
@Ignore
class TVSetSpec extends FeatureSpec with GivenWhenThen {
  info("As a TV set owner")
  info("I want to be able to turn the TV on and off")
  info("So I can watch TV when I want")
  info("And save energy when I'm not watching TV")
  import org.scalatest.Tag
  object TvTest extends Tag("TVTest")

  feature("TV power button") {
    scenario("User presses power button when TV is off",TvTest) {
      Given("a TV set that is switched off")
      val tv = new TVSet
      assert(!tv.isOn)
      When("the power button is pressed")
      tv.pressPowerButton()
      Then("the TV should switch on")
      assert(tv.isOn)
    }
    scenario("User presses power button when TV is on") {
      Given("a TV set that is switched on")
      val tv = new TVSet
      tv.pressPowerButton()
      assert(tv.isOn)
      When("the power button is pressed")
      tv.pressPowerButton()
      Then("the TV should switch off")
      assert(!tv.isOn)
    }
  }
}



