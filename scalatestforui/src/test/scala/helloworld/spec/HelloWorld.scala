
package helloworld.spec

import java.util.concurrent.TimeUnit

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}
import org.openqa.selenium.{By, TimeoutException, WebDriver, WebElement}
import org.scalatest.concurrent.Eventually
import org.scalatest.selenium.WebBrowser
import org.scalatest.time.{Seconds, Span}
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

  val implicitTimeout = 20
  val scriptTimeout = 30
  val pageLoadTimeout = 30

  //implicit override val patienceConfig = PatienceConfig(timeout = scaled(Span(50, Seconds)), interval = scaled(Span(1, Seconds)))
  implicit override val patienceConfig = PatienceConfig(timeout = Span(50, Seconds), interval = Span(1, Seconds))
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

    // NEVER USE IMPLICIT AND EXPLICIT WAITS AT THE SAME TIME. IMPLICIT WAIT OVERRIDES EXPLICIT WAITS AND WHEN USED TOGETHER RESULTS IN CONFUSING BEHAVIOUR
    // http://stackoverflow.com/questions/15164742/combining-implicit-wait-and-explicit-wait-together-results-in-unexpected-wait-ti?__s=mt1thrrnxzar9qq41eep#answer-15174978?utm_medium=email&utm_campaign=elemental-selenium-ruby&utm_source=47-waiting
    // http://stackoverflow.com/questions/10404160/when-to-use-explicit-wait-vs-implicit-wait-in-selenium-webdriver

    scenario(s"Search for a Hotel from Homepageof UK",Smoke) {

     assume (isSiteUp,"HealthCheck failed")
     Given("The user lands on the homepage")

      go to host

      //isPageFinishedLoading shouldBe true // NOT A GOOD PRACTICE

      assertResult(Succeeded,"Home page loading takes more time ") { eventually { isPageFinishedLoading shouldBe true  } } //GOOD Practice to use Eventually and assertResult

      pageTitle should include("Travel Deals")

      When("The user enters a destination")
      var hotelTab: WebElement = null //Try(webDriver.findElement(By.id("tab-hotel-tab"))).getOrElse(null)
      assertResult(Succeeded,"Hotel Tab button is not present") {
        eventually {
          hotelTab = Try(webDriver.findElement(By.id("tab-hotel-tab"))).getOrElse(null)
          hotelTab should not be null
        }
      }
      click on hotelTab

      /*ScalaTest's WebBrowser trait provides an easy and efficient way for manipulating input fields on the page. The trait supports the HTML5 elements as well,
      like tel input type. For each input field type, there is a method for querying it, like if you search for a single selection, you can use the singleSel method.
      There are two method definitions for each type, one with a single String parameter and one with a Query parameter. By default, the one with String parameter tries
      to find the element by Id matching the parameter, and waits until the configured implicit wait timeout. Then it tries to find by the input field's name attribute.
      This can slow down the test, so there is a better approach for using this feature efficiently. This WebBrowser trait contains several Query types as NameQuery, CssQuery,
      etc. which can be used for exact element matching. Long story short, there is an example how to use these embedded input fields:
       */
      //var destination: TextField = Try(textField("hotel-destination")(webDriver)).getOrElse(null) // NOT GOOD PRACTICE
      var destination: TextField = Try(textField(IdQuery("hotel-destination"))).getOrElse(null) //GOOD PRACTICE

      destination.value ="Las Vegas"
      //textField(IdQuery("hotel-destination")).value = "Las Vegas" // You can also do this

      closeTypeAhead //Best practice - handling all nuisance and unexpected , transient behaviours

      And ("Click on search")
      val searchButton: WebElement = Try(webDriver.findElement(By.id("search-button"))).getOrElse(null)
      click on searchButton


      Then ("User should land on Hotel Search Results page")
      assertResult(Succeeded,"HSR page loading takes more time ") { eventually { isPageFinishedLoading shouldBe true  } } //GOOD Practice to use Eventually and assertResult

     // validation without Checkpoints
      currentUrl.contains("Hotel-Search")
      //val summary: WebElement = Try(webDriver.findElement(By.className("playback-summary-data"))).getOrElse(null) //GOOD PRACTICE
      //assertResult(SucceededStatus,"Search button is shown even after clicking it") {eventually { summary.isDisplayed shouldBe true}}

      //val cp = new Checkpoint // good practice to use Checkpoint
      //cp {currentUrl.contains("Hotel-Search")}
       //val summary: WebElement = Try(webDriver.findElement(By.id("wizardSummaryStartDate"))).getOrElse(null) //GOOD PRACTICE
      //cp {assertResult(Succeeded,"HSR date info is not found"){ summary.isDisplayed shouldBe true} }
      //cp.reportAll()
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

