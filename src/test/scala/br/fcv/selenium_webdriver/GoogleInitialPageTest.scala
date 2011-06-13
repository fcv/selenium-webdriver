package br.fcv.selenium_webdriver

import org.junit.runner.RunWith
import org.openqa.selenium.By.{id, name}
import org.openqa.selenium.{NoSuchElementException => NoSuchWebElementException, WebDriver}
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import support.RichWebDriver.conversions._

@RunWith(classOf[JUnitRunner])
class GoogleInitialPageTest extends WebDriverFixtureFunSuite with ShouldMatchers {
    
    test("testing") { driver =>
        driver get "http://www.google.com.br"
        
        val elm = driver \ id("main") \ id("body") \ id("lga") \ name("name-not-found") \ name("test")
        
        val exception = intercept[NoSuchWebElementException] {
            elm !
        }        
        
        exception.getMessage should (include ("name-not-found") and not include ("teste"))
    }
}