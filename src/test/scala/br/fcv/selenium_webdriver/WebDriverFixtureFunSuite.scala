package br.fcv.selenium_webdriver

import org.scalatest.fixture.FixtureFunSuite
import org.openqa.selenium.WebDriver
import org.scalatest.Tag
import scala.collection.GenTraversable

trait WebDriverFixtureFunSuite extends FixtureFunSuite { 
    
    type FixtureParam = WebDriver
    
    def withFixture(test: OneArgTest) {
        val browserType = WebDriverFixtureFunSuite.decode(test.name)
        
        val driver = BrowserType.newWebDriver( 
                browserType getOrElse { 
                    throw new IllegalStateException("Browser info coud not be decode from test name: " + test.name) 
                } 
            );
        
        try {
            initialUrl foreach { driver get _ }            
            test(driver)
        } finally {
            driver.quit
        }
    }
    
    protected override def test(testName: String, testTags: Tag*)(testFun: FixtureParam => Any) {
        this.test(testName, browsers, testTags: _*)(testFun)
    }
    
    protected def test(testName: String, browsers: Traversable[BrowserType.Value], testTags: Tag*)(testFun: FixtureParam => Any) {
        browsers foreach { (b => 
            super.test(WebDriverFixtureFunSuite.encode(testName, b), testTags: _*)(testFun));
        }
    }
    
    protected def initialUrl: Option[String] = {
        val className = this.getClass.getSimpleName
        val url = this.getClass.getResource("/" + className + "-page.html");
        Some(url) map { _.toString }
    }
    
    protected def browsers: Traversable[BrowserType.Value] = BrowserType.values

}

object WebDriverFixtureFunSuite {
    
    def encode(testName: String, browser: BrowserType.Value): String = browser.toString + ": " + testName
    
    def decode(testName: String): Option[BrowserType.Value] = { 
        try { 
            Option(BrowserType.withName( testName takeWhile { _ != ':' } )) 
        } catch {
        	case e: NoSuchElementException => None
        }
    }
    
}