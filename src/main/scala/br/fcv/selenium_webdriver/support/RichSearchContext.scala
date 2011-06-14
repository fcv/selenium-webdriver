package br.fcv.selenium_webdriver.support

import org.openqa.selenium.{By, NoSuchElementException => NoSuchWebElementException, SearchContext, WebElement}
import scala.collection.JavaConversions._

abstract class RichSearchContext(context: SearchContext) {
    
	def \ (by: By): ElementBox = {
		try {
		    Full(root, context findElement by)
		} catch {
		    case e: NoSuchWebElementException => Empty(root, by)
		}
	}
	
	def *\ (by: By): List[WebElement] = (context findElements by).toList 
	
	protected def root: List[WebElement] 

}