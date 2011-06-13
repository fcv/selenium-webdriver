package br.fcv.selenium_webdriver

import br.fcv.selenium_webdriver.support.implicits._
import org.junit.runner.RunWith
import org.openqa.selenium.By.{id, name, tagName}
import org.openqa.selenium.{NoSuchElementException => NoSuchWebElementException, WebDriver, WebElement}
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class FindElementTest extends WebDriverFixtureFunSuite with ShouldMatchers {
    
    private def localPageUrl = getClass.getResource("/test-page.html").toString
    
    test("testing exception message seaching on webdriver") { driver =>
        driver get localPageUrl
        
        val elm = driver \ tagName("body") \ id("main")  \ name("content") \ name("name-not-found") \ name("test")
        
        val exception = intercept[NoSuchWebElementException] {
            elm !
        }        
        
        exception.getMessage should (include ("name-not-found") and not include ("teste"))
    }
    
    test("testing exception message seaching on webelement") { driver =>
        driver get (localPageUrl)
        
        val main: WebElement = driver \ tagName("body") \ id("main") !        
        val teste = main \ name("content") \ name("name-not-found") \ name("teste")
        
        val exception = intercept[NoSuchWebElementException] {
            teste !
        }        
        
        exception.getMessage should (include ("name-not-found") and not include ("teste"))
    }
    
    
}