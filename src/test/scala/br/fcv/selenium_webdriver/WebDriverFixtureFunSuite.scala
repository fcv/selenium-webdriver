package br.fcv.selenium_webdriver

import org.scalatest.fixture.FixtureFunSuite
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver

trait WebDriverFixtureFunSuite extends FixtureFunSuite {
    
    final override type FixtureParam = WebDriver
    
    final override def withFixture(test: OneArgTest) {
        val webdriver: WebDriver = createWebDriver;
        try {
        	test(webdriver)
        } finally {
            webdriver.quit
        }
    }
    
    def createWebDriver = new FirefoxDriver

}