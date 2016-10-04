
package helloworld.spec

import java.util.concurrent.TimeUnit

import helloworld.spec.pagemodel.HomePage
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}
import org.scalatest.time.{Seconds, Span}

//import org.openqa.selenium.firefox.MarionetteDriver
import org.openqa.selenium.{TimeoutException, By, WebDriver, WebElement}
import org.scalatest._

import scala.util.Try

/**
  * Created by rambighananthan on 9/26/16.
  */


class Lesson2 extends AcceptanceSpec with ParallelTestExecution  {

  object L1 extends Tag("com.expedia.l1")
  object L2 extends Tag("com.expedia.l2")

  val implicitTimeout = 10
  val scriptTimeout = 30
  val pageLoadTimeout = 30

  System.setProperty("webdriver.chrome.driver","./src/test/resources/chromedriver")
  implicit val webDriver: WebDriver = new ChromeDriver()
  implicit override val patienceConfig = PatienceConfig(timeout = scaled(Span(120, Seconds)), interval = scaled(Span(1, Seconds)))


  val host = "http://www.expedia.co.uk/"
  info("Making sure that the docweb site is working ")

  feature("Hotel Search") {

    val timeouts = webDriver.manage().timeouts()
    timeouts.pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS)
    timeouts.setScriptTimeout(scriptTimeout, TimeUnit.SECONDS)
    timeouts.implicitlyWait(implicitTimeout, TimeUnit.SECONDS)

    scenario(s"Search for a Hotel from Homepageof UK", L1) {

      Given("The user lands on the homepage")
      go to host
      assertResult(true, "Home page loading takes more time ") {
        eventually(timeout(scaled(Span(120, Seconds))), interval(scaled(Span(5, Seconds)))) {
          isPageFinishedLoading
        }
      }

      When("The user enters a destination")
      val hotelTab: WebElement = Try(webDriver.findElement(By.id("tab-hotel-tab"))).getOrElse(null)
      click on hotelTab

      var destination: TextField = Try(textField("hotel-destination")(webDriver)).getOrElse(null)
      //textField("hotel-destination").value = "Las Vegas"
      destination.value = "Las Vegas"

      closeTypeAhead

      pageTitle should include("Travel Deals")

      And("Click on search")
      val searchButton: WebElement = Try(webDriver.findElement(By.id("search-button"))).getOrElse(null)
      click on searchButton

      Then("User should land on Hotel Search Results page")
      currentUrl.contains("Hotel-Search")
    }


    scenario(s"Search for a Hotel from Homepageof UK 2", L2) {

      assume (isSiteUp,"HealthCheck failed")

      Given("The user lands on the homepage")
      go to host
      assertResult(true, "Home page loading takes more time ") {
        eventually(timeout(scaled(Span(120, Seconds))), interval(scaled(Span(5, Seconds)))) {
          isPageFinishedLoading
        }
      }

      When("The user enters a destination")
      val homePageHotelTab: WebElement = Try(webDriver.findElement(By.id("tab-hotel-tab"))).getOrElse(null)
      eventually {
        homePageHotelTab.isDisplayed
      }

      click on homePageHotelTab
      var destinationText: TextField = Try(textField("hotel-destination")(webDriver)).getOrElse(null)
      //textField("hotel-destination").value = "Las Vegas"
      destinationText.value = "Las Vegas"
      closeTypeAhead

      //based on elements check the status using eventually
      val checkbox = webDriver.findElement(By.id("hotel-add-flight-checkbox"))
      eventually {
        assertResult(Succeeded, "Check box to select Add a flight is not selected by default") {
          checkbox.isSelected shouldBe false
        }
      }
      //set and verify for a reliable automation
      val adult: SingleSel = Try(singleSel("hotel-1-adults")(webDriver)).getOrElse(null)
      adult.value = "1"
     //verify what you set immediately
      assertResult(Succeeded, "Numbe of adults should be 1") {
        adult.value shouldBe "1"
      }

     val homePage = new HomePage(webDriver)
     //bad way to assert
     homePage.hotelSearchWizard.adult.isDisplayed shouldBe true
     homePage.hotelSearchWizard.checkinDate.isDisplayed shouldBe true
     homePage.hotelSearchWizard.destination.isDisplayed shouldBe true

     //best practice for assert
     val cp = new Checkpoint

       cp {assertResult(Succeeded,"Adult not displayed"){ homePage.hotelSearchWizard.adult.isDisplayed shouldBe true }}
       cp {assertResult(Succeeded,"checkin date is not displayed") {homePage.hotelSearchWizard.checkinDate.isDisplayed shouldBe true}}
       cp {assertResult(Succeeded,"HotelsearchDestination is not displayed") { homePage.hotelSearchWizard.destination.isDisplayed shouldBe true}}

       cp.reportAll()

      And("Click on search")
      val searchButton: WebElement = Try(webDriver.findElement(By.id("search-button"))).getOrElse(null)
      click on searchButton

      Then("User should land on Hotel Search Results page")
      currentUrl.contains("Hotel-Search")

    }

    def isPageFinishedLoading(): Boolean = {
      val isPageLoaded = Try(executeScript("return document.readyState == 'complete'")(webDriver).toString().toBoolean).getOrElse(false)
      val isUitkLoaded = Try(executeScript("return typeof uitk!='undefined' && uitk.readyState")(webDriver).toString().toBoolean).getOrElse(false)
      //val isAjaxCallsEmpty = Try(executeScript("return jQuery.active == 0")(webDriver).toString().toBoolean).getOrElse(false)
      isPageLoaded && isUitkLoaded
    }

  }
  override def afterEach() {
    try {
      super.afterEach()
    } finally {
      if (null != webDriver) {
        webDriver.quit()
      }
    }
  }

  override def afterAll() {
    try {
      super.afterAll()
    } finally {
      if (null != webDriver) {
        webDriver.quit()
      }
    }
  }
  def isSiteUp() = {

   go to "http://www.expedia.co.uk/isWorking"
   eventually {
     pageSource.contains("Status is success")
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

