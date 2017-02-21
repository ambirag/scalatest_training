package helloworld.spec.pagemodel

/**
  * Created by rambighananthan on 2/21/17.
  */

import helloworld.spec.AcceptanceSpec
import org.openqa.selenium.{By, WebDriver, WebElement}
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Seconds, Span}

class HotelSearchResultsPage(driver : WebDriver) extends AcceptanceSpec with Eventually  {
  val pageIdValue = "page.Hotel-Search"
  val pageURL = "Hotel-Search?"

  implicit override val patienceConfig = PatienceConfig(timeout = scaled(Span(30, Seconds)), interval = scaled(Span(1, Seconds)))

  def firstHotelButton(): WebElement = {
    driver.findElements(By.className("flex-link-wrap")).get(1)
  }

  def firstHotelButtonClick(): Unit ={
    click on firstHotelButton()
  }

  def isHotelSRPage: Boolean = {
    val page = driver.getPageSource
    val pageName = "page.Hotels.Search"
    page.contains("logging.pageName = \"" +pageName)
  }

  def pointsValue(): WebElement = {
    firstHotelButton().findElement(By.className("points")).findElement(By.className("value"))
  }

  def cashValue(): WebElement = {
    firstHotelButton().findElement(By.className("actualPrice"))
  }

}

