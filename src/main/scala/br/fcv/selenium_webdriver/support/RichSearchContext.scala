package br.fcv.selenium_webdriver.support

import org.openqa.selenium.{By, NoSuchElementException => NoSuchWebElementException, SearchContext, WebElement}

abstract class RichSearchContext(context: SearchContext) {
    
	def \ (by: By): ElementBox = {
		try {
		    Full(root, context findElement by)
		} catch {
		    case e: NoSuchWebElementException => Empty(root, by)
		}
	}
	
	protected def root: List[WebElement] 

}