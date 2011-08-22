package br.fcv.selenium_webdriver

import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.junit.JUnitRunner
import org.openqa.selenium.JavascriptExecutor


@RunWith(classOf[JUnitRunner])
class JQuerySelector extends WebDriverFixtureFunSuite with ShouldMatchers {

    override val browsers = List(BrowserType.Firefox)
    
    test("test jquery selector") { webdriver =>
    
        //-- based on: http://www.vcskicks.com/selenium-jquery.php
        val jsDriver = webdriver.asInstanceOf[JavascriptExecutor]
        jsDriver executeScript """
        	var head = document.getElementsByTagName('head')[0];
        	var newScript = document.createElement('script');
        	newScript.type = 'text/javascript';
        	newScript.src = 'http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js';
        	head.appendChild(newScript);
        	"""   
        
        // -- returns a j.u.List<WebElement>
        // -- browsers just hangs if i only return $('#main') that is way i'm converting it to array
        val result = jsDriver executeScript "return $('#main').toArray(); " ;
                        
    }
    
    
    
}