package br.fcv.selenium_webdriver

import br.fcv.selenium_webdriver.support.implicits._
import org.junit.runner.RunWith
import org.openqa.selenium.By
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class WebElementListTest extends WebDriverFixtureFunSuite with ShouldMatchers {
    
    override val browsers = BrowserType.Firefox :: Nil
    
    test("test sum all <li> elements") { driver => 
    	
		val sum = driver *\ By.className("main") *\ By.tagName("li") map { _.getText.toInt } sum
		val resultElm = driver \ By.id("result")
		
		sum should be === resultElm.getText.toInt		
    }
    
    test("test filter even elements") { driver =>
        
        val evens = driver *\ By.className("main") *\ By.tagName("li") map { _.getText.toInt } filter isEven
        
        evens should be === (2 to 10 by 2)
    }

    private def isEven(i: Int) = i % 2 == 0
    
}