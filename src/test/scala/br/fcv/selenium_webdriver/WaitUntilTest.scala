package br.fcv.selenium_webdriver

import br.fcv.selenium_webdriver.support.implicits._
import br.fcv.selenium_webdriver.support.ElementBox
import org.junit.runner.RunWith
import org.openqa.selenium.By.{className, id, name, tagName}
import org.openqa.selenium.{By, NoSuchElementException => NoSuchWebElementException, TimeoutException, WebElement}
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class WaitUntilTest extends WebDriverFixtureFunSuite with ShouldMatchers {
    
    override val browsers = List(BrowserType.Firefox)

    test("failure") { driver =>
        
        val button = driver \ id("button")        
        button.click
        
        intercept[NoSuchWebElementException] {
        	val ul = driver \ id("my-ul") !
        }
    }
    
    test("waiting") { driver =>
        
        val button = driver \ id("button");        
        button.click;
        
        val ul = driver.waitUntil(2) { d => d \ id("my-ul") ! };
        
        val lis = ul *\ tagName("li");
        lis.size should be === 10;        
    }
    
    test("[Own wait impl] waiting") { driver =>
        val button = driver \ id("button");        
        button.click;
        
        import br.fcv.selenium_webdriver.support.experimental.Waiter   
        
        val wait = new Waiter(500, 4000)
        
        val ul = wait until { driver \ id("my-ul") };
        
        val lis = ul *\ tagName("li");
        lis.size should be === 10;
    }
    
    test("[Own wait impl] failure by timeout") { driver =>
        
        val button = driver \ id("button")        
        // button.click
        import br.fcv.selenium_webdriver.support.experimental.Waiter        
        val wait = new Waiter(50, 1000)
        
        intercept[TimeoutException] {            
        	val ul = wait until { driver \ id("my-ul") }
        }
    }
    
}