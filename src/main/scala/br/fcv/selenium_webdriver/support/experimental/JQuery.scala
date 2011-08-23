package br.fcv.selenium_webdriver.support.experimental

import org.openqa.selenium.By
import java.util.{List => JList}
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement
import org.openqa.selenium.JavascriptExecutor
import grizzled.slf4j.Logging

/**
 * 
 * Specific kind of By that uses jquery to select elements.
 * 
 * At this time target (tested) page is required to contain jquery js
 * file. A good improvement would be to automatically append it when
 * it is not present.
 * 
 */
class JQuery(expression: String) extends By with Logging {

    //-- based on: http://www.vcskicks.com/selenium-jquery.php

    def findElements(context: SearchContext): JList[WebElement] = {
        debug("entering JQuery.findElements")
    	val wait = new Waiter(checkPeriod = 500, timeout = 4000)
    	
    	val jsExecutor = context.asInstanceOf[JavascriptExecutor];
    	
        wait until {
    		val result = (jsExecutor executeScript JQuery.expressiontWithJQueryCheck(expression))
    		debug("jquery selector '%s' returned %s of type %s ".format(
    		        expression, 
    		        result, 
    		        if (result == null) "null" else result.getClass.getName))

    		//-- if result is a boolean it means that jquery was not present
    		if (result.isInstanceOf[Boolean])
    		    None
		    else 
		        Some(result.asInstanceOf[JList[WebElement]])    		
    	}
    }
    
    //-- TODO: escape javascript quotes
    def find(selector: String) = new JQuery(expression + ".find('" + selector + "')");
    
    override val toString = expression

}

object JQuery {
    
    //-- TODO: escape javascript quotes
    def apply(selector: String) = new JQuery( "jQuery('" + selector + "')" ); 
    
    //-- TODO: define a way to parameterize jquery source
    private def expressiontWithJQueryCheck(expression: String) = """
    		var idNewScript = 'jQuery-selenium-by-test';
    		if (window.jQuery == null && document.getElementById(idNewScript) == null) {
    			var head = document.getElementsByTagName('head')[0];
        		var newScript = document.createElement('script');
        		newScript.type = 'text/javascript';
    			newScript.id = idNewScript;
        		newScript.src = 'http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js';
        		head.appendChild(newScript);
    		}
    		var isJQueryPresent = window.jQuery != null;
    		if (isJQueryPresent) {
    			return """ + expression + """.toArray();
    		} else {
    			return false;
    		}
    	"""
    
}