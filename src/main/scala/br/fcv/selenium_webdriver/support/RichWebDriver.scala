package br.fcv.selenium_webdriver.support

import org.openqa.selenium.{By, WebDriver}
import org.openqa.selenium.{NoSuchElementException => NoSuchHtmlElementException}

class RichWebDriver(driver: WebDriver) {    
    
    def tryFindElement(by: By) = RichWebDriver tryFindElement (driver, by)
    
    def \\ (by: By) = driver findElement by
    
    def ?\ (by: By) = RichWebDriver tryFindElement (driver, by)

}

object RichWebDriver {
    
    def tryFindElement(driver: WebDriver, by: By) = {
    	try {
    		Some(driver findElement by)
    	} catch {
    	    case e: NoSuchHtmlElementException => None
    	}
    }
    
    object conversions {        
        implicit def enrichWebDriver(driver: WebDriver) = new RichWebDriver(driver);        
    }
    
}