package scalatest
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.scalatest._
import org.scalatest.selenium.WebBrowser
object Functional extends FlatSpec with Matchers with WebBrowser {
  implicit val webDriver: WebDriver = new FirefoxDriver()
  go to "http://www.expedia.co.uk/"
  click on id("tab-hotel-tab")
  textField("hotel-destination").value = "Las Vegas"
  click on id("search-button")
  pageTitle contains ("sadf Deal")
}