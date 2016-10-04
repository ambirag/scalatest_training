
package helloworld.spec

import org.openqa.selenium.firefox.FirefoxDriver

//import org.openqa.selenium.firefox.MarionetteDriver
import org.openqa.selenium.{By, WebDriver, WebElement}
import org.scalatest._

import scala.util.Try

/**
  * Created by rambighananthan on 9/26/16.
  */
//ParallelTestExecution

class Lesson2 extends AcceptanceSpec  {

  object L1 extends Tag("com.expedia.l1")
  object L2 extends Tag("com.expedia.l2")

  val implicitTimeout = 20
  val scriptTimeout = 30
  val pageLoadTimeout = 30


  implicit val webDriver: WebDriver = new FirefoxDriver()
  /* val timeouts = webDriver.manage().timeouts()
  timeouts.pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS)
  timeouts.setScriptTimeout(scriptTimeout, TimeUnit.SECONDS)
  timeouts.implicitlyWait(0, TimeUnit.SECONDS) **/

  val host = "http://www.expedia.ie/"
  info("Making sure that the docweb site is working ")

  feature("Hotel Search") {
    scenario(s"Search for a Hotel from Homepageof UK", L1) {
      Given("The user lands on the homepage")
      go to host
      assertResult(true, "Home page loading takes more time ") {
        eventually {
          Try(executeScript("return document.readyState == 'complete'")(webDriver).toString().toBoolean).getOrElse(false)
        }
      }

      When("The user enters a destination")
      val hotelTab: WebElement = Try(webDriver.findElement(By.id("tab-hotel-tab"))).getOrElse(null)
      click on hotelTab
      var destination: TextField = Try(textField("hotel-destination")(webDriver)).getOrElse(null)
      //textField("hotel-destination").value = "Cheese!"
      destination.value = "Las Vegas"
      pageTitle should include("Hotels: from cheap")

      And("Click on search")
      val searchButton: WebElement = Try(webDriver.findElement(By.id("search-button"))).getOrElse(null)
      click on searchButton

      Then("User should land on Hotel Search Results page")
      currentUrl.contains("Hotel-Search")

    }

 /*   scenario(s"Search for a Hotel from Homepageof UK 2", L2) {
      implicitlyWait(Span(10, Seconds))

      Given("The user lands on the homepage")
      go to host
      assertResult(true, "Home page loading takes more time ") {

        eventually(timeout(Span(5, Seconds)), interval(Span(5, Millis))) {
          Try(executeScript("return document.readyState == 'complete'")(webDriver).toString().toBoolean).getOrElse(false)
        }
      }
      //accessing href text
      Try(webDriver.findElement(By.className("nectar-logo-text"))).getOrElse(null) shouldBe ("Collect Nectar points")
      //click on href link
      click on linkText("Collect Nectar points.")

      val checkbox = webDriver.findElement(By.id("hotel-add-flight-checkbox"))
      assertResult(true, "Check box to select Add a flight is not selected by default") {
        checkbox.isSelected shouldBe false
      }

      val adult: SingleSel = Try(singleSel("hotel-1-adults")(webDriver)).getOrElse(null)
      adult.value = "2"
      assertResult(true, "Numbe of adults should be 2") {
        adult.value shouldBe "2"
      }

      val notification: WebElement = Try(webDriver.findElement(By.className("menu sticky"))).getOrElse(null)
      notification.click()
      Thread.sleep(2000)
      notification.click()

      When("The user enters a destination")
      val hotelTab: WebElement = Try(webDriver.findElement(By.id("tab-hotel-tab"))).getOrElse(null)
      click on hotelTab
      var destination: TextField = Try(textField("hotel-destination")(webDriver)).getOrElse(null)
      //textField("hotel-destination").value = "Cheese!"
      destination.value = "Las Vegas"
      pageTitle should include("Hotels: from cheap")

      And("Click on search")
      val searchButton: WebElement = Try(webDriver.findElement(By.id("search-button"))).getOrElse(null)
      click on searchButton

      Then("User should land on Hotel Search Results page")
      currentUrl.contains("Hotel-Search")

    }*/
  }

  override def afterAll() {
    try {
      super.afterAll
    } finally {
      if (null != webDriver) {
        webDriver.quit()
      }
    }
  }
}

