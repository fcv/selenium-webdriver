package br.fcv.selenium_webdriver.support

import com.google.common.base.Function
import org.openqa.selenium.{By, WebDriver}
import org.openqa.selenium.{NoSuchElementException => NoSuchHtmlElementException}
import org.openqa.selenium.support.ui.WebDriverWait

class RichWebDriver(driver: WebDriver) extends RichSearchContext(driver)  {   
    
   override val root = Nil
   
   def waitUntil[T](timeout: Long, sleepTime: Long = 1000)(cond: WebDriver => T): T = {
       val wait = new WebDriverWait(driver, timeout, sleepTime);
       wait.until(new Function[WebDriver, T] {
           def apply(driver: WebDriver) = cond(driver)
       });
   }   

}

object RichWebDriver {  
    
    object conversions {        
        implicit def enrichWebDriver(driver: WebDriver) = new RichWebDriver(driver);        
    }
    
}