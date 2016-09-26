
package helloworld.spec

import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.{By, WebDriver,WebElement}
import org.openqa.selenium.firefox.FirefoxDriver
import org.scalatest.{FeatureSpec, _}
import org.scalatest.selenium.WebBrowser

import scala.util.Try

/**
  * Created by rambighananthan on 9/26/16.
  */
class HelloWorld extends FeatureSpec with GivenWhenThen with Matchers with WebBrowser {

   implicit val webDriver: WebDriver = new FirefoxDriver()

    val host = "http://www.expedia.co.uk/"
     info("Making sure that the docweb site is working ")

    feature("Hotel Search") {
      scenario(s"Search for a Hotel from Homepageof UK") {
      Given("The user lands on the homepage")
        go to host
        assertResult(true,"Home page loading takes more time ") {
          Try(executeScript("return document.readyState == 'complete'")(webDriver).toString().toBoolean).getOrElse(false)
        }

        When("The user enters a destination")
         val hotelTab: WebElement =Try(webDriver.findElement(By.id("tab-hotel-tab"))).getOrElse(null)
         click on hotelTab
         var destination: TextField = Try(textField("hotel-destination")(webDriver)).getOrElse(null)
         new Actions(webDriver).moveToElement(webDriver.findElement(By.id("hotel-destination"))).perform();
         destination.value ="Las Vegas"
         pageTitle should include("Travel Deals")

         And ("Click on search")
         val searchButton: WebElement = Try(webDriver.findElement(By.id("search-button"))).getOrElse(null)
         click on searchButton

         Then ("User should land on Hotel Search Results page")
         currentUrl.contains("Hotel-Search")

    }
    }

}

