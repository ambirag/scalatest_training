
package helloworld.spec

import java.util.concurrent.TimeUnit

import helloworld.spec.pagemodel.HomePage
import org.openqa.selenium.chrome.ChromeDriver

//import org.openqa.selenium.firefox.MarionetteDriver
import org.openqa.selenium.{By, WebDriver, WebElement}
import org.scalatest._

import scala.util.Try

/**
  * Created by rambighananthan on 9/26/16.
  */


class Lesson2 extends AcceptanceSpec with ParallelTestExecution  {

  object L1 extends Tag("com.expedia.l1")
  object L2 extends Tag("com.expedia.l2")

  val implicitTimeout = 20
  val scriptTimeout = 30
  val pageLoadTimeout = 30


  implicit val webDriver: WebDriver = new ChromeDriver()
   val timeouts = webDriver.manage().timeouts()
  timeouts.pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS)
  timeouts.setScriptTimeout(scriptTimeout, TimeUnit.SECONDS)
  timeouts.implicitlyWait(0, TimeUnit.SECONDS)

  val host = "http://www.expedia.co.uk/"
  info("Making sure that the docweb site is working ")

  feature("Hotel Search") {
    ignore(s"Search for a Hotel from Homepageof UK", L1) {
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


    scenario(s"Search for a Hotel from Homepageof UK 2", L2) {

      assume (isSiteUp,"HealthCheck failed")

      Given("The user lands on the homepage")
      go to host
      assertResult(true, "Home page loading takes more time ") {
        eventually {
          Try(executeScript("return document.readyState == 'complete'")(webDriver).toString().toBoolean).getOrElse(false)
        }
      }

      When("The user enters a destination")
      val homePageHotelTab: WebElement = Try(webDriver.findElement(By.id("tab-hotel-tab"))).getOrElse(null)
      click on homePageHotelTab
      var destinationText: TextField = Try(textField("hotel-destination")(webDriver)).getOrElse(null)
      //textField("hotel-destination").value = "Las Vegas"
      destinationText.value = "Las Vegas"

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
}

