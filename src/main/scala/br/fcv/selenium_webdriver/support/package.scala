package br.fcv.selenium_webdriver

import br.fcv.selenium_webdriver.support.{ RichWebDriver, RichWebElement }
import org.openqa.selenium.{ WebDriver, WebElement }
import org.openqa.selenium.By

package object support {

    object implicits {
        implicit def enrichWebElement(element: WebElement): RichWebElement = new RichWebElement(element);
        implicit def enrichWebDriver(driver: WebDriver): RichWebDriver = new RichWebDriver(driver);

        implicit def box2element(box: ElementBox): WebElement = box.get
    }

}