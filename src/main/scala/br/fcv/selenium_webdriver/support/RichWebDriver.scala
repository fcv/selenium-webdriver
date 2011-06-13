package br.fcv.selenium_webdriver.support

import org.openqa.selenium.{By, WebDriver}
import org.openqa.selenium.{NoSuchElementException => NoSuchHtmlElementException}

class RichWebDriver(driver: WebDriver) extends RichSearchContext(driver)  {   
    
   override val root = Nil

}

object RichWebDriver {  
    
    object conversions {        
        implicit def enrichWebDriver(driver: WebDriver) = new RichWebDriver(driver);        
    }
    
}