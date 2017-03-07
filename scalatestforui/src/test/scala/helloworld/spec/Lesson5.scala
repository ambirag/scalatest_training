package helloworld.spec

import java.util.concurrent.TimeUnit

import com.expedia.www.commons.expweb.helper.{ExpwebUriBuilder, PointsOfSale}
import helloworld.spec.pagemodel.HomePage
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}
import org.openqa.selenium.{By, OutputType, TakesScreenshot, TimeoutException, WebDriver, WebElement}
import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.Tables.Table
import org.scalatest.time.{Millis, Seconds, Span}
//import org.openqa.selenium.firefox.MarionetteDriver
import scala.util.{Random, Try}

/**
  * Created by rambighananthan on 3/07/17.
  */

class Lesson5 extends AcceptanceSpec1 with ExpwebUriBuilder with ParallelTestExecution  {

  object l9 extends Tag("com.expedia.l9")


  val implicitTimeout = 30
  val scriptTimeout = 30
  val pageLoadTimeout = 30

  System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver")
  System.setProperty("environment", "LIVE")

  implicit val webDriver: WebDriver = new ChromeDriver()
  implicit override val patienceConfig = PatienceConfig(timeout = Span(30, Seconds), interval = Span(200, Millis))


  info("Making sure that the docweb site is working ")
  feature("Hotel Search") {
    val timeouts = webDriver.manage().timeouts()
    timeouts.pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS)
    timeouts.setScriptTimeout(scriptTimeout, TimeUnit.SECONDS)
    timeouts.implicitlyWait(implicitTimeout, TimeUnit.SECONDS)

    val searchData = Table(("pos"), (PointsOfSale.en_GB))
    forAll(searchData) {
      (pos: PointsOfSale.PointOfSale) =>

        scenario(s"Search for a Hotel from Homepageof UK without using fixtures - $pos", l9) {
          //assume(isSiteUp, "HealthCheck failed")
          Given("The user lands on the homepage")
          go to buildUri(pos)
          //assertResult(Succeeded, "Home page loading takes more time ") {
            eventually {
              isPageFinishedLoading shouldBe true
            }
         // }
          When("The user enters a destination")
          val homePage = new HomePage(webDriver)
          assertResult(true, "home page SW takes more time to show hotels tab") {
            eventually {
              homePage.lobTabs.hotelTab.get.isDisplayed
            }
          }

          click on homePage.lobTabs.hotelTab.get

          homePage.hotelSearchWizard.destination.value = "Las Vegas"
          closeTypeAhead
          homePage.hotelSearchWizard.adult.value = "1"
          //verify what you set immediately
          assertResult(Succeeded, "Numbe of adults should be 1") {
            homePage.hotelSearchWizard.adult.value shouldBe "1"
          }

          homePage.hotelSearchWizard.checkinDate.value = "02/04/2017"
          homePage.hotelSearchWizard.checkoutDate.value = "04/04/2017"

          //based on elements check the status using eventually
          //val checkbox = webDriver.findElement(By.id("hotel-add-flight-checkbox"))
          assertResult(Succeeded, "Check box to select Add a flight is not selected by default") {
            eventually {
              {
                homePage.hotelSearchWizard.addFlight.isSelected shouldBe false
              }
            }
          }
          //bad way to assert
          homePage.hotelSearchWizard.adult.isDisplayed shouldBe true
          homePage.hotelSearchWizard.checkinDate.isDisplayed shouldBe true
          homePage.hotelSearchWizard.destination.isDisplayed shouldBe true

          //best practice for assert
          val cp = new Checkpoint

          cp {
            assertResult(Succeeded, "Adult not displayed") {
              homePage.hotelSearchWizard.adult.isDisplayed shouldBe true
            }
          }
          cp {
            assertResult(Succeeded, "checkin date is not displayed") {
              homePage.hotelSearchWizard.checkinDate.isDisplayed shouldBe true
            }
          }
          cp {
            assertResult(Succeeded, "HotelsearchDestination is not displayed") {
              homePage.hotelSearchWizard.destination.isDisplayed shouldBe true
            }
          }
          cp.reportAll()

          And("Click on search")
          //waitUntilElementExist(homePage.hotelSearchWizard.searchButton, webDriver)
          click on homePage.hotelSearchWizard.searchButton
          waitForPageInterstitials()

          Then("User should land on Hotel Search Results page")
          assertResult(true, "invalid url:" + currentUrl) {
            currentUrl.contains("Hotel-Search")
          }
        }

    }
    //====================================================
    def isPageFinishedLoading(): Boolean = {
      val isPageLoaded = Try(executeScript("return document.readyState == 'complete'")(webDriver).toString().toBoolean).getOrElse(false)
      val isUitkLoaded = Try(executeScript("return typeof uitk!='undefined' && uitk.readyState")(webDriver).toString().toBoolean).getOrElse(false)
      //val isAjaxCallsEmpty = Try(executeScript("return jQuery.active == 0")(webDriver).toString().toBoolean).getOrElse(false)
      print("isPageLoaded:" + isPageLoaded + "isUitkLoaded:" + isUitkLoaded)
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

    go to "https://www.expedia.co.uk/isWorking"
    eventually {
      pageSource.contains("Status is success")
    }
  }

  def closeTypeAhead = {

    val typeAheadClose: WebElement = Try(webDriver.findElement(By.id("typeahead-close"))).getOrElse(null)
    try {
      val wait = new WebDriverWait(webDriver, 5)
      wait.until(ExpectedConditions.elementToBeClickable(By.id("typeahead-close")))
      if (typeAheadClose != null) {
        typeAheadClose.click
      }
    }
    catch {
      case e: TimeoutException => e
      case b: Exception => b
      case n: NullPointerException => n
    }
  }

  def waitUntilElementVisibile(element: WebElement, webDriver: WebDriver) = {

    val wait = new WebDriverWait(webDriver, 10)
    assertResult(Succeeded, "Element s\"${element}\" not visible") {
      wait.until(ExpectedConditions.visibilityOf(element))
    }

  }

  def waitUntilElementExist(element: WebElement, webDriver: WebDriver) = {

    val wait = new WebDriverWait(webDriver, 10)
    assertResult(Succeeded, "Element s\"${element}\" not present") {
      wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(s"${element}")))
    }
  }

  def waitForPageInterstitials() = {

    try {
      val waitForInterstitial = new WebDriverWait(webDriver, 20)
      waitForInterstitial.until(ExpectedConditions.presenceOfElementLocated(By.id("pageInterstitial")))
    } catch {
      case e: TimeoutException => e
      case f: org.openqa.selenium.TimeoutException => f
      case b: Exception => b
      case n: NullPointerException => n
    }
  }


  def fixture()= new {
    val destination = "Las Vegas"
    val adult = "1"
    val children = "Test"
    val checkinDate = "02/04/2017"
    val checkoutDate = "04/04/2017"

    def fillHotelSearch(homePage: HomePage) = {

      homePage.hotelSearchWizard.adult.value=adult
      //verify what you set immediately
      assertResult(Succeeded, "Numbe of adults should be 1") {
        homePage.hotelSearchWizard.adult.value shouldBe adult
      }

      homePage.hotelSearchWizard.destination.value=destination
      closeTypeAhead
      homePage.hotelSearchWizard.checkinDate.value=checkinDate
      homePage.hotelSearchWizard.checkoutDate.value=checkoutDate
    }
  }

  override def withFixture(test: NoArgTest) = {
    super.withFixture(test) match {
      case failed: Failed =>
        val screenshot = webDriver.asInstanceOf[TakesScreenshot].getScreenshotAs(OutputType.BASE64)
        val randomId: Int = new Random().nextInt(1000) + 100
        markup(s"TEST FAILED<br /><a onclick=\42toggleDetails('img${randomId}', '')\42 href='#'>Show/Hide Screenshot</a><div id='img${randomId}' style='display:none;'><img alt='Embedded Image' src='data:image/png;base64,${screenshot.toString()}'/></div>")
        failed
      case other => other
    }
  }

}

