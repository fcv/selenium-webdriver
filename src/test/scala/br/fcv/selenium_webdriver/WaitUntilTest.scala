package br.fcv.selenium_webdriver

import org.scalatest.matchers.ShouldMatchers
import br.fcv.selenium_webdriver.support.implicits._
import org.openqa.selenium.By.{className, id, name, tagName}
import org.openqa.selenium.{ NoSuchElementException => NoSuchWebElementException }
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.openqa.selenium.WebElement
import org.openqa.selenium.By

@RunWith(classOf[JUnitRunner])
class WaitUntilTest extends WebDriverFixtureFunSuite with ShouldMatchers {

    test("testing failure") { driver =>
        
        val button = driver \ id("button")        
        button.click
        
        intercept[NoSuchWebElementException] {
        	val ul = driver \ id("my-ul") !
        }
    }
    
    test("testing waiting") { driver =>
        
        val button = driver \ id("button")        
        button.click
        
        val ul = driver.waitUntil(2000) { d => d \ id("my-ul") ! }
        
        val lis = ul *\ tagName("li")
        lis.size should be === 10
    }
    
}