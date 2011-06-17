package br.fcv.selenium_webdriver.experimental

import org.junit.runner.RunWith
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.By.id
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

import br.fcv.selenium_webdriver.support.implicits._

@RunWith(classOf[JUnitRunner])
class JsPromptTest extends WebDriverFixtureFunTest with ShouldMatchers {
    
    test("send value and retrieve it", List(BrowserType.Firefox)) { driver =>
    
        var input = driver \ id("my-input") !
        
        val jsExecutor = driver.asInstanceOf[JavascriptExecutor]
        jsExecutor executeScript {
        	"""
        	var value = prompt('Please, set a value to input text');
			var i = document.getElementById('my-input');
			i.value = value; 
        	"""             
        }
        
        val alert = driver.switchTo.alert 
        
        val content = "my-value";
        alert.sendKeys(content)
        alert.accept
        
        //-- note that input has been reached before its value is changed
        // but value attribute is refreshed
        input.getAttribute("value") should be === content 
    }

}