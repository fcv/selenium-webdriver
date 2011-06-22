package br.fcv.selenium_webdriver

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.openqa.selenium.WebDriver
import org.scalatest.fixture.FixtureFunSuite
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.By

import scala.collection.JavaConversions._
import util.control.Breaks._

@RunWith(classOf[JUnitRunner])
class GoogleSuggestions extends FixtureFunSuite {
    
    type FixtureParam = WebDriver
    
    def withFixture(test: OneArgTest) {
        val webdriver: WebDriver = new FirefoxDriver();
        try {
        	test(webdriver)
        } finally {
            webdriver.quit
        }
    }
    
    test("testing with firefox") { driver =>
        // based on example from: http://code.google.com/p/selenium/wiki/GettingStarted
        
        // And now use this to visit Google
        driver get "http://www.google.com/webhp?complete=1&hl=en"
                
        // Find the text input element by its name
        val element = driver findElement By.name("q")

        // Enter something to search for
        element.sendKeys("Cheese!")

        // Sleep until the div we want is visible or 5 seconds is over
        val end: Long = System.currentTimeMillis + 5000;
        
        breakable {
	        while (System.currentTimeMillis < end) {
	            // Browsers which render content (such as Firefox and IE) return "RenderedWebElements"
	            val resultsDiv  = driver findElement By.className("gac_m");
	
	            // If results have been returned, the results are displayed in a drop down.
	            if (resultsDiv.isDisplayed()) {
	            	break
	            }
	        }
        }
        
        driver findElements By.xpath("//td[@class='gac_c']") foreach { e => 
        	println(e.getText)
        }        
    }  
    

}