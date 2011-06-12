package br.fcv.selenium_webdriver

import org.junit.runner.RunWith
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.{By, NoSuchElementException => NoSuchWebElementException}
import org.openqa.selenium.WebDriver
import org.scalatest.fixture.FixtureFunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import scala.collection.JavaConversions._
import scala.util.control.Breaks._
import support.RichWebDriver.conversions._

@RunWith(classOf[JUnitRunner])
class GoogleInitialPage extends FixtureFunSuite with ShouldMatchers {

    type FixtureParam = WebDriver
    
    def withFixture(test: OneArgTest) {
        val webdriver: WebDriver = new FirefoxDriver();
        try {
        	test(webdriver)
        } finally {
            webdriver.quit
        }
    }
    
    test("testing") { driver =>
        driver get "http://www.google.com.br"
        
        val elm = driver \ By.id("main") \ By.id("body") \ By.id("lga") \ By.name("name-not-found") \ By.name("test")
        
        val exception = intercept[NoSuchWebElementException] {
            elm !
        }        
        
        exception.getMessage should (include ("name-not-found") and not include ("teste"))
    }
}