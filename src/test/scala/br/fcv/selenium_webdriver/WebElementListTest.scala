package br.fcv.selenium_webdriver

import br.fcv.selenium_webdriver.support.implicits._
import org.junit.runner.RunWith
import org.openqa.selenium.{By, WebElement}
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
    
    test("using for comprehension") { driver =>
        
        val mains = driver *\ By.className("main")
        
        //- WebElementList and withFilter 
        val r1: Iterable[Int] = for (main <- mains; 
        		h1 <- main *\ By.tagName("h1") if (h1.getText == "high");
        		li <- main *\ By.tagName("li")) 
            		yield li.getText.toInt 
        
        r1 should be === (List(6, 7, 8, 9, 10))     
        
        //- WebElementList along with ElementBox
        val r2: Iterable[String] = for (main <- driver *\ By.className("main");
                		li <- main \ By.tagName("li")) 
            				yield li.getText 
        // note that 'main \ By.tagName("li")' will return only the first element 
        r2 should be === ( List("1", "6") )
        
        //- WebElementList, withFilter and ElementBox
        val r3: Iterable[String] = for (main <- mains; 
        		h1 <- main *\ By.tagName("h1") if (h1.getText == "high");
        		li <- main *\ By.tagName("li")) 
            		yield li.getText
            		
        r3 should be === ( 6 to 10 map { _.toString } )
        
        //- just checking expected type.. this would led to a compilation error.. so no check is needed
        val r4: Iterable[WebElement] = for (main <- mains; 
        		h1 <- main *\ By.tagName("h1") if (h1.getText == "high");
        		li <- main *\ By.tagName("li")) 
            		yield li
        
        
    }
    
}