package br.fcv.selenium_webdriver.experimental
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver

object BrowserType extends Enumeration {
	val Firefox = Value("Firefox")
    val HtmlUnit = Value("HtmlUnit")
    
    def newWebDriver(bt: BrowserType.Value) = {
	    bt match {
	        case Firefox => new FirefoxDriver
	        case HtmlUnit => new HtmlUnitDriver
	    }
	}
}