package br.fcv.selenium_webdriver

import br.fcv.selenium_webdriver.support.implicits._
import br.fcv.selenium_webdriver.support.{Empty, Full}
import org.junit.runner.RunWith
import org.openqa.selenium.By.{id, name, tagName}
import org.openqa.selenium.{NoSuchElementException => NoSuchWebElementException, WebDriver, WebElement}
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.By

@RunWith(classOf[JUnitRunner])
class FindElementTest extends WebDriverFixtureFunSuite with ShouldMatchers {
    
    override def createWebDriver = new HtmlUnitDriver
    
    test("testing exception message seaching on webdriver") { driver =>
        
        val elm = driver \ tagName("body") \ id("main")  \ name("content") \ name("name-not-found") \ name("test")
        
        val exception = intercept[NoSuchWebElementException] {
            elm !
        }        
        
        exception.getMessage should (include ("name-not-found") and not include ("teste"))
    }
    
    test("testing exception message seaching on webelement") { driver =>
        
        val main: WebElement = driver \ tagName("body") \ id("main") !        
        val teste = main \ name("content") \ name("name-not-found") \ name("teste")
        
        val exception = intercept[NoSuchWebElementException] {
            teste !
        }        
        
        exception.getMessage should (include ("name-not-found") and not include ("teste"))
    }
    
    test("testing foreach on empty") { driver =>
        
        val box = driver \ tagName("body") \ id("main")  \ name("content") \ name("name-not-found")
        box foreach { elm:WebElement => fail("empty box should not excecute foreach") }        
    }
    
    test("testing for on empty") { driver =>
    
        val box = driver \ tagName("body") \ id("main")  \ name("content") \ name("name-not-found")
        
        for (elm: WebElement <- box) {
        	fail("empty box should not excecute for") 
        }
        
    }
    
    test("test filter map and flatMap methods") { driver =>
        
        val box = driver \ id("body")
        
        (for (e <- box if e.getAttribute("class") == "div-class-body") yield e) match {
            case Empty() => fail("filter was expected to result in something")
            case _ => 
        }
          
        (for (e <- box if e.getAttribute("class") == "div-class-body-wrong") yield e) match {
            case Full() => fail("filter was expected to result in empty result")
            case _ => 
        }
        
        (for (e <- box 
                if e.getAttribute("class") == "div-class-body";
        			f <- e \ name("content")) yield f) match {
            case Empty() => fail("filter was expected to result in something")
            case _ =>
        }
        
        (for (e <- box 
                if e.getAttribute("class") == "div-class-body";
        			f <- e \ By.className("div-class")) yield f) match {
            case Empty() => fail("filter was expected to result in something")
            case _ =>
        }
        
        (for (e <- box if e.getAttribute("class") == "div-class-body";
        			f <- e \ By.className("div-class") if f.getAttribute("title") == "my-title") yield f) match {
            case Empty() => fail("filter was expected to result in something")
            case _ =>
        }
        
        (for (e <- box if e.getAttribute("class") == "div-class-body";
        			f <- e \ By.className("div-class") if f.getAttribute("title") == "my_title") yield f) match {
            case Full() => fail("filter was expected to result in nothing")
            case _ =>
        }
    }
        
}