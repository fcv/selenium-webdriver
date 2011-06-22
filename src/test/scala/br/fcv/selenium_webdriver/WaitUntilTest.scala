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
        import br.fcv.selenium_webdriver.support.experimental.Waiter.TimeImplicits._
        val wait = new Waiter(checkPeriod = 50 miliseconds, timeout = 2 seconds)
        
        intercept[TimeoutException] {            
        	val ul = wait until { driver \ id("my-ul") }
        }
    }
    
    test("[Own wait impl] don't wait too much") { driver =>
        
        val button = driver \ id("button")        
        // button.click -- do not click 
        import br.fcv.selenium_webdriver.support.experimental.Waiter   
        import br.fcv.selenium_webdriver.support.experimental.Waiter.TimeImplicits._
        val wait = new Waiter(checkPeriod = 3 seconds, timeout = 1 seconds)
        
        import System.currentTimeMillis
        
        val start = currentTimeMillis
        
        intercept[TimeoutException] {            
        	val ul = wait until { driver \ id("my-ul") }
        }
        val timeEllapsed = currentTimeMillis - start
        
        timeEllapsed should (be >= (1000L) and be < (1500L))
    }
    
}