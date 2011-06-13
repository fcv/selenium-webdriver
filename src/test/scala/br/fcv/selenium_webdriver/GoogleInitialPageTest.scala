package br.fcv.selenium_webdriver

import br.fcv.selenium_webdriver.support.implicits._
import org.junit.runner.RunWith
import org.openqa.selenium.By.{id, name}
import org.openqa.selenium.{NoSuchElementException => NoSuchWebElementException, WebDriver, WebElement}
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class GoogleInitialPageTest extends WebDriverFixtureFunSuite with ShouldMatchers {
    
    test("testing exception message seaching on webdriver") { driver =>
        driver get "http://www.google.com.br"
        
        val elm = driver \ id("main") \ id("body") \ id("lga") \ name("name-not-found") \ name("test")
        
        val exception = intercept[NoSuchWebElementException] {
            elm !
        }        
        
        exception.getMessage should (include ("name-not-found") and not include ("teste"))
    }
    
    test("testing exception message seaching on webelement") { driver =>
        driver get "http://www.google.com.br"
        
        val body: WebElement = driver \ id("main") \ id("body") !        
        val teste = body \ id("lga") \ name("name-not-found") \ name("teste")
        
        val exception = intercept[NoSuchWebElementException] {
            teste !
        }        
        
        exception.getMessage should (include ("name-not-found") and not include ("teste"))
    }
    
    
}