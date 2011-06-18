package br.fcv.selenium_webdriver

import org.scalatest.matchers.ShouldMatchers
import br.fcv.selenium_webdriver.support.implicits._
import org.openqa.selenium.By.{className, id, name, tagName}
import org.openqa.selenium.{ NoSuchElementException => NoSuchWebElementException }
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import br.fcv.selenium_webdriver.support.ElementBox

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
        implicit def box2option(box: ElementBox) = box.toOption
        val wait = new Waiter(500, 4000)
        
        val ul = wait until { driver \ id("my-ul") };
        
        val lis = ul *\ tagName("li");
        lis.size should be === 10;
    }
    
}