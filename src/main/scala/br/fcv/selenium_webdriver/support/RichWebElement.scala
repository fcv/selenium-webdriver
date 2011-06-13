package br.fcv.selenium_webdriver.support

import org.openqa.selenium.{By, WebElement}
import org.openqa.selenium.{NoSuchElementException => NoSuchWebElementException}

class RichWebElement(element: WebElement) extends RichSearchContext(element) {
    
    override val root = List(element)
    
}

object RichWebElement {
  
	object conversions {        
        implicit def enrichWebElement(element: WebElement) = new RichWebElement(element);        
    }

}