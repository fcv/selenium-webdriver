package br.fcv.selenium_webdriver.support

import org.openqa.selenium.{By, NoSuchElementException => NoSuchWebElementException, SearchContext, WebElement}
import scala.collection.JavaConversions._
import experimental.WebElementList

abstract class RichSearchContext(context: SearchContext) {
    
	def \ (by: By): ElementBox = {
		try {
		    Full(root, context findElement by)
		} catch {
		    case e: NoSuchWebElementException => Empty(root, by)
		}
	}
	
	def *\ (by: By): WebElementList = WebElementList((context findElements by).toList: _*) 
	
	protected def root: List[WebElement] 

}