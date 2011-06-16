package br.fcv.selenium_webdriver.experimental

import org.scalatest.fixture.FixtureFunSuite
import org.openqa.selenium.WebDriver

trait WebDriverFixtureFunTest extends FixtureFunSuite {
    
    type FixtureParam = WebDriver
    
    def withFixture(test: OneArgTest) {
        encode("as", BrowserType.Chrome)
    }
    
    def encode(testName: String, browser: BrowserType.Value): String = browser.toString + ":" + testName
    
    def decode(testName: String): Option[BrowserType.Value] = { 
        try { 
            Option(BrowserType.withName( testName takeWhile { _ != ':' } )) 
        } catch {
        	case e: NoSuchElementException => None
        }
    }

}