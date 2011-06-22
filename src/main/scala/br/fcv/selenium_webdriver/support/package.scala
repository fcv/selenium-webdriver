package br.fcv.selenium_webdriver

import br.fcv.selenium_webdriver.support.{ RichWebDriver, RichWebElement }
import org.openqa.selenium.{ WebDriver, WebElement }
import org.openqa.selenium.By

package object support {

    object implicits {
        implicit def enrichWebElement(element: WebElement) = new RichWebElement(element);
        implicit def enrichWebDriver(driver: WebDriver) = new RichWebDriver(driver);

        implicit def box2element(box: ElementBox) = new WebElement {
            private def elm = box !
            def click = elm click
            def submit = elm submit            
            def sendKeys(keysToSend: CharSequence*) = elm sendKeys (keysToSend: _*);
            def clear = elm clear
            def getTagName = elm getTagName;
            def getAttribute(name: String) = elm getAttribute name;
            def toggle() = elm toggle;
            def isSelected = elm isSelected;
            def setSelected() = elm setSelected;
            def isEnabled = elm isEnabled;
            def getText = elm getText;
            def findElements(by: By) = elm findElements by;
            def findElement(by: By) = elm findElement by;
            def isDisplayed = elm.isDisplayed;
            def getLocation = elm.getLocation;
            def getSize = elm.getSize;
            def getCssValue(propertyName: String) = elm getCssValue propertyName
        }
    }

}