package br.fcv.selenium_webdriver

import scala.collection.JavaConversions.asScalaBuffer

import org.junit.runner.RunWith
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.{NoSuchElementException => NoSuchWebElementException}
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

import br.fcv.selenium_webdriver.support.experimental.{JQuery => $}

@RunWith(classOf[JUnitRunner])
class JQuerySelector extends WebDriverFixtureFunSuite with ShouldMatchers {

    override val browsers = List(BrowserType.Firefox)
    
    test("test 'jquery by' object") { webdriver =>
        
        val labelElms = webdriver.findElements( $("#main").find("label") )       
        val labels =  labelElms map { elm => elm.getText }
        
        labels should be === List("l1", "l2")        
    }
    
    test("test 'jquery by' error") { webdriver =>
        
        val exception = intercept[NoSuchWebElementException] {

            //-- tries to find an inexistent element 
            webdriver.findElement( $("#main").find("textarea") )
        }
        
        exception.getMessage should include ("jQuery('#main').find('textarea')")
    }  
    
}