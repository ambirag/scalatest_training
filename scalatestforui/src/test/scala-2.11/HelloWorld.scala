/*
package helloworld.spec


import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxProfile}
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.remote.{Augmenter, DesiredCapabilities, RemoteWebDriver}
import org.openqa.selenium.safari.SafariDriver
import org.scalatest.FeatureSpec
import org.scalatest.matchers
/**
  * Created by rambighananthan on 9/26/16.
  */
class HelloWorld extends GivenWhenThen with ShouldMatchers with Matchers with WebBrowser {

  implicit val webDriver: WebDriver = new FirefoxDriver

  val host = "http://www.expedia.co.uk/"

  "The blog app home page" should "have the correct title" in {
    go to (host + "index.html")
    pageTitle should be("Awesome Blog")
  }

}
*/
