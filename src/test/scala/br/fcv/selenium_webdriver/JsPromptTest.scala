package br.fcv.selenium_webdriver

import org.junit.runner.RunWith
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.By.id
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

import br.fcv.selenium_webdriver.support.implicits._

@RunWith(classOf[JUnitRunner])
class JsPromptTest extends WebDriverFixtureFunSuite with ShouldMatchers {
    
    private def localPageUrl = getClass.getResource("/JsPromptTest-page.html").toString
    
    test("send value and retrieve it") { driver =>
        
        driver get localPageUrl
        
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
        
        input.getAttribute("value") should be === content 
    }

}