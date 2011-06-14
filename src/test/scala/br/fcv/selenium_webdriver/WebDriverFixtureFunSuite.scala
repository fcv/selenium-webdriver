package br.fcv.selenium_webdriver

import org.scalatest.fixture.FixtureFunSuite
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver

trait WebDriverFixtureFunSuite extends FixtureFunSuite {
    
    final override type FixtureParam = WebDriver
    
    final override def withFixture(test: OneArgTest) {
        val webdriver: WebDriver = createWebDriver;
        try {
            initialUrl foreach { url => webdriver get url}            
        	test(webdriver)
        } finally {
            webdriver.quit
        }
    }
    
    def createWebDriver:WebDriver = new FirefoxDriver
    
    protected def initialUrl: Option[String] = {
        val className = this.getClass.getSimpleName
        val url = this.getClass.getResource("/" + className + "-page.html");
        Some(url) map { _.toString }
    }

}