package br.fcv.selenium_webdriver.support

import org.openqa.selenium.{By, WebElement}
import org.openqa.selenium.{NoSuchElementException => NoSuchWebElementException}

object RichWebElement {
  
	def tryFindElement(elem: WebElement, by: By) = {
		try {
			Some(elem findElement by)
		} catch {
		  case e: NoSuchWebElementException => None
		}
	}

}