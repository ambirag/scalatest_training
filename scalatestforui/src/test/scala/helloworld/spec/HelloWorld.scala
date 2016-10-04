
package helloworld.spec

//import org.openqa.selenium.firefox.MarionetteDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.{By, WebDriver, WebElement}
import org.scalatest.concurrent.Eventually
import org.scalatest.selenium.WebBrowser
import org.scalatest.{FeatureSpec, _}

import scala.util.Try

/**
  * Created by rambighananthan on 9/26/16.
  */

abstract class AcceptanceSpec extends FeatureSpec with GivenWhenThen with Matchers with WebBrowser with Checkpoints with Eventually with BeforeAndAfterEach with BeforeAndAfterAll {
  override val invokeBeforeAllAndAfterAllEvenIfNoTestsAreExpected = true
}
@Ignore
class HelloWorld extends AcceptanceSpec {


  implicit val webDriver: WebDriver = new FirefoxDriver() //FirefoxDriver()
  object Smoke extends Tag("com.expedia.Smoke")

  val host = "http://www.expedia.ie/"
  info("Making sure that the docweb site is working ")

  feature("Hotel Search") {
    scenario(s"Search for a Hotel from Homepageof UK",Smoke) {
     Given("The user lands on the homepage")

      go to host
      assertResult(true,"Home page loading takes more time ") {
        eventually {
          Try(executeScript("return document.readyState == 'complete'")(webDriver).toString().toBoolean).getOrElse(false)
        }
      }

      When("The user enters a destination")
      val hotelTab: WebElement =Try(webDriver.findElement(By.id("tab-hotel-tab"))).getOrElse(null)
      click on hotelTab
      var destination: TextField = Try(textField("hotel-destination")(webDriver)).getOrElse(null)
      //new Actions(webDriver).moveToElement(webDriver.findElement(By.id("hotel-destination"))).perform();
      //textField("hotel-destination").value = "Cheese!"
      destination.value ="Las Vegas"
      pageTitle should include("Hotels: from cheap")

      And ("Click on search")
      val searchButton: WebElement = Try(webDriver.findElement(By.id("search-button"))).getOrElse(null)
      click on searchButton

      Then ("User should land on Hotel Search Results page")
      currentUrl.contains("Hotel-Search")

     }
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

  override def afterEach() {
    try {
      super.afterEach
    } finally {
      if (null != webDriver) {
        webDriver.quit()
      }
    }
  }
}

