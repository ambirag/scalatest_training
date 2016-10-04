
package helloworld.spec

//import org.openqa.selenium.firefox.MarionetteDriver
import java.util.concurrent.TimeUnit

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}
import org.openqa.selenium.{TimeoutException, By, WebDriver, WebElement}
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

class HelloWorld extends AcceptanceSpec {

  val implicitTimeout = 20
  val scriptTimeout = 30
  val pageLoadTimeout = 30

  implicit val webDriver: WebDriver = new ChromeDriver() //FirefoxDriver()
  object Smoke extends Tag("com.expedia.Smoke")

  val host = "http://www.expedia.co.uk/"
  info("Making sure that the docweb site is working ")

  feature("Hotel Search") {


    val timeouts = webDriver.manage().timeouts()
    timeouts.pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS)
    timeouts.setScriptTimeout(scriptTimeout, TimeUnit.SECONDS)
    timeouts.implicitlyWait(implicitTimeout, TimeUnit.SECONDS)


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
      closeTypeAhead
      pageTitle should include("Travel Deals")

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

  def closeTypeAhead = {

    val typeAheadClose: WebElement = Try(webDriver.findElement(By.id("typeahead-close"))).getOrElse(null)
    try {
      val wait = new WebDriverWait(webDriver, 5)
      wait.until(ExpectedConditions.elementToBeClickable(By.id("typeahead-close")))
      if (typeAheadClose != null)
      {
        typeAheadClose.click
      }
    }
    catch {
      case e: TimeoutException => e
      case b: Exception => b
      case n: NullPointerException => n
    }

  }
}

