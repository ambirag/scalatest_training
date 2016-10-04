package helloworld.spec.pagemodel

import helloworld.spec.AcceptanceSpec
import org.openqa.selenium.{By, WebDriver, WebElement}

import scala.util.Try

/**
  * Created by rambighananthan on 10/4/16.
  */
class HomePage(driver: WebDriver) extends AcceptanceSpec {

  object hotelSearchWizard{
    def adult: SingleSel = Try(singleSel("hotel-1-adults")(driver)).getOrElse(null)
    def children: SingleSel = Try(singleSel("hotel-1-children")(driver)).getOrElse(null)
    def destination: TextField = Try(textField("hotel-destination")(driver)).getOrElse(null)
    def checkinDate: TextField = Try(textField("hotel-checkin")(driver)).getOrElse(null)
    def checkoutDate: TextField = Try(textField("hotel-checkout")(driver)).getOrElse(null)
    def typeAheadClose: WebElement =Try(driver.findElement(By.id("typeahead-close"))).getOrElse(null)
    def homePageSearchWizard: WebElement = Try(driver.findElement(By.id("new-homepage-search-wizard"))).getOrElse(null)
    def rooms: SingleSel = Try(singleSel("hotel-rooms")(driver)).getOrElse(null)
    def childAge1: SingleSel = Try(singleSel("hotel-1-age-select-1")(driver)).getOrElse(null)
    def childAge2: SingleSel = Try(singleSel("hotel-1-age-select-2")(driver)).getOrElse(null)
  }

}
