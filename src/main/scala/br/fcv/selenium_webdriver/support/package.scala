package br.fcv.selenium_webdriver

import br.fcv.selenium_webdriver.support.{RichWebDriver, RichWebElement}
import org.openqa.selenium.{WebDriver, WebElement}

package object support {
    
    object implicits {
        implicit def enrichWebElement(element: WebElement) = new RichWebElement(element);
        implicit def enrichWebDriver(driver: WebDriver) = new RichWebDriver(driver);
    }
    
}