package helloworld.spec.pagemodel

//import helloworld.spec.AcceptanceSpec
import com.expedia.www.commons.expweb.pagemodel.Page
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}
import org.openqa.selenium.{By, WebDriver, WebElement}

import scala.util.Try

/**
  * Created by rambighananthan on 2/21/17.
  */

class HomePage(driver: WebDriver) extends Page(driver)  {

  val pageIdValue = "Homepage"
  val pageURL = ""

  def hotelTabClick(): Unit ={
    val hotelTab: WebElement =Try(driver.findElement(By.id("tab-hotel-tab"))).getOrElse(null)
    click on hotelTab
  }

  def flightTabClick(): Unit = {
    val flightTab: WebElement = Try(driver.findElement(By.id("tab-flight-tab"))).getOrElse(null)
    click on flightTab
  }

  def flightSearchClick(elementName : String): Unit = {
    val flightSearch : WebElement = Try(driver.findElement(By.id(elementName))).getOrElse(null)
    click on flightSearch
  }

  def carTabClick(): Unit = {
    val carTab: WebElement = Try(driver.findElement(By.id("tab-car-tab"))).getOrElse(null)
    click on carTab
  }

  def vacationPackageTabClick(): Unit = {
    val vacationTab: WebElement = Try(driver.findElement(By.id("primary-header-package"))).getOrElse(null)
    click on vacationTab
  }

  def searchButtonClick(): Unit = {
    val searchButton: WebElement = Try(driver.findElement(By.id("search-button"))).getOrElse(null)
    click on searchButton
  }

  implicit def waitForElement(locator: By) : WebElement = {
      val predicate: WebDriver => WebElement = _.findElement(locator)
      new WebDriverWait(driver, 20).withMessage(s"waiting for element '$locator' on page '${driver.getCurrentUrl}'").until(ExpectedConditions.presenceOfElementLocated(locator))
  }

  def searchButtonClick2(): Unit = {
    val searchButton = By.id("hotel-add-flight-checkbox-hp-hotel")
    searchButton.click()

  }

  def activitiesTabClick(): Unit ={
    val ActivityTab: WebElement =Try(driver.findElement(By.id("tab-activity-tab"))).getOrElse(null)
    click on ActivityTab
  }

  def tripsSummaryClick(): Unit ={
    val MyTripsTab: WebElement = Try(driver.findElement(By.id("utility-link"))).getOrElse(null)
    click on MyTripsTab
  }

  object activitiesSearchWizard{
    val activitiesDestination: TextField = Try(textField("activity-destination")(driver)).getOrElse(null)
    val activitiesFromDate: TextField = Try(textField("activity-start")(driver)).getOrElse(null)
    val activitiesToDate: TextField = Try(textField("activity-end")(driver)).getOrElse(null)
  }

  object interstitial{
    val pageInterstitial: WebElement = Try(driver.findElement(By.id("pageInterstitial"))).getOrElse(null)
  }

  object formError {
    val destinationError: WebElement = Try(driver.findElement(By.className("destinationError"))).getOrElse(null)
  }

  def isHomePage: Boolean = {
    val page = driver.getPageSource
    page.contains("pageId = " + pageIdValue ) || page.contains ("logging.pageName = \"" + pageIdValue)

  }
  def enterText(): Unit ={
    var addFlightText = By.id("hotel-flight-origin-hp-hotel")
    addFlightText.sendKeys("Las Vegas")
  }
  object hotelSearchWizard{
    val adult: SingleSel = Try(singleSel(IdQuery("hotel-1-adults-hp-hotel"))(driver)).getOrElse(null)
    val children: SingleSel = Try(singleSel("hotel-1-children")(driver)).getOrElse(null)
    val destination: TextField = Try(textField(IdQuery("hotel-destination-hp-hotel"))(driver)).getOrElse(null)
    val checkinDate: TextField = Try(textField(IdQuery("hotel-checkin-hp-hotel"))(driver)).getOrElse(null)
    val checkoutDate: TextField = Try(textField(IdQuery("hotel-checkout-hp-hotel"))(driver)).getOrElse(null)
    val typeAheadClose: WebElement =Try(driver.findElement(By.className("datepicker-close"))).getOrElse(null)
    val homePageSearchWizard: WebElement = Try(driver.findElement(By.id("wizard-tabs"))).getOrElse(null)
    val rooms: SingleSel = Try(singleSel("hotel-rooms")(driver)).getOrElse(null)
    val childAge1: SingleSel = Try(singleSel("hotel-1-age-select-1")(driver)).getOrElse(null)
    val childAge2: SingleSel = Try(singleSel("hotel-1-age-select-2")(driver)).getOrElse(null)
    val searchButton: WebElement = Try(driver.findElement(By.xpath("//*[@id=\"gcw-hotel-form-hp-hotel\"]/div[7]/label/button"))).getOrElse(null)
    val addFlight: Checkbox = Try(checkbox("hotel-add-flight-checkbox")(driver)).getOrElse(null)
    val h1: WebElement = Try(driver.findElement(By.tagName("h1"))).getOrElse(null)


  }

  object flightSearchWizard {
    val adult: SingleSel = Try(singleSel("flight-adults")(driver)).getOrElse(null)
    val children: SingleSel = Try(singleSel("flight-children")(driver)).getOrElse(null)
    val departure: TextField = Try(textField("flight-origin")(driver)).getOrElse(null)
    val destination: TextField = Try(textField("flight-destination")(driver)).getOrElse(null)
    val departureTime: TextField = Try(textField("flight-departing")(driver)).getOrElse(null)
    val returnTime: TextField = Try(textField("flight-returning")(driver)).getOrElse(null)
    val departure2: TextField = Try(textField("flight-2-origin")(driver)).getOrElse(null)
    val destination2: TextField = Try(textField("flight-2-destination")(driver)).getOrElse(null)
    val departureTime2: TextField = Try(textField("flight-2-departing")(driver)).getOrElse(null)
    val typeAheadClose: WebElement =Try(driver.findElement(By.id("typeahead-close"))).getOrElse(null)
    val homePageSearchWizard: WebElement = Try(driver.findElement(By.id("new-homepage-search-wizard"))).getOrElse(null)
  }

  object hotelLobTabs {
    val hotelLobTab: WebElement =Try(driver.findElement(By.id("tab-hotel-tab-hp"))).getOrElse(null)

  }
  object lobTabs {
    val hotelTab: Option[WebElement] =Try(driver.findElement(By.id("tab-hotel-tab-hp"))).toOption
    hotelTab match {
      case Some(field) => field.getTagName
      case None =>
    }
  }

}
