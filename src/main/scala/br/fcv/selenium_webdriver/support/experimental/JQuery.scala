package br.fcv.selenium_webdriver.support.experimental

import org.openqa.selenium.By
import java.util.{List => JList}
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement
import org.openqa.selenium.JavascriptExecutor

/**
 * 
 * Specific kind of By that uses jquery to select elements.
 * 
 * At this time target (tested) page is required to contain jquery js
 * file. A good improvement would be to automatically append it when
 * it is not present.
 * 
 */
class JQuery(expression: String) extends By {

    //-- based on: http://www.vcskicks.com/selenium-jquery.php

    def findElements(context: SearchContext): JList[WebElement] = {
        
        context
        	.asInstanceOf[JavascriptExecutor]
        	.executeScript("return " + expression + ".toArray();")
        	.asInstanceOf[JList[WebElement]]
    }
    
    //-- TODO: escape javascript quotes
    def find(selector: String) = new JQuery(expression + ".find('" + selector + "')");
    
    override val toString = expression

}

object JQuery {
    
    //-- TODO: escape javascript quotes
    def apply(selector: String) = new JQuery( "jQuery('" + selector + "')" );    
    
}