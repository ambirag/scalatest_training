
package helloworld.spec

import java.util.concurrent.TimeUnit

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}
import org.openqa.selenium.{By, TimeoutException, WebDriver, WebElement}
import org.scalatest.concurrent.Eventually
import org.scalatest.selenium.WebBrowser
import org.scalatest.{FeatureSpec, _}

import scala.util.Try

/**
  * Created by rambighananthan on 9/26/16.
  * ScalaTest Lession 2
  * Tags; Querying other Elements: Parallel / Sequential Execution / Before and After Hooks / Quiv vs CLose / Ignore Scenario / Ignore Feature / Regular Asserts vs CheckPoints with AssertResults
  *
  */


abstract class AcceptanceSpec extends FeatureSpec with GivenWhenThen with Matchers with WebBrowser with Checkpoints with Eventually with BeforeAndAfterEach with BeforeAndAfterAll {
  override val invokeBeforeAllAndAfterAllEvenIfNoTestsAreExpected = true

}

class HelloWorld extends AcceptanceSpec {

  val implicitTimeout = 10
  val scriptTimeout = 30
  val pageLoadTimeout = 30

  System.setProperty("webdriver.chrome.driver","./src/test/resources/chromedriver")
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

     assume (isSiteUp,"HealthCheck failed")
     Given("The user lands on the homepage")

      go to host
      assertResult(Succeeded,"Home page loading takes more time ") { eventually { isPageFinishedLoading shouldBe true  } }

      When("The user enters a destination")
      val hotelTab: WebElement =Try(webDriver.findElement(By.id("tab-hotel-tab"))).getOrElse(null)
      click on hotelTab
      var destination: TextField = Try(textField("hotel-destination")(webDriver)).getOrElse(null)
      //textField("hotel-destination").value = "Las Vegas"
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
      val wait = new WebDriverWait(webDriver, 3)
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
  def isSiteUp() = {

    go to "http://www.expedia.co.uk/isWorking"
    eventually {
      pageSource.contains("Status is success")
    }
  }

  def isPageFinishedLoading(): Boolean = {
    val isPageLoaded = Try(executeScript("return document.readyState == 'complete'")(webDriver).toString().toBoolean).getOrElse(false)
    val isUitkLoaded = Try(executeScript("return typeof uitk!='undefined' && uitk.readyState")(webDriver).toString().toBoolean).getOrElse(false)
    //val isAjaxCallsEmpty = Try(executeScript("return jQuery.active == 0")(webDriver).toString().toBoolean).getOrElse(false)
    isPageLoaded && isUitkLoaded
  }
}

