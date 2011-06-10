package org.openqa.selenium.lift;

import static org.openqa.selenium.lift.Finders.*;
import static org.openqa.selenium.lift.Matchers.*;

import static org.hamcrest.Matchers.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class GoogleTest extends HamcrestWebDriverTestCase {

  @Override
  protected WebDriver createDriver() {
    return new FirefoxDriver();
  }
        
  public void testHasAnImageSearchPage() throws Exception {
                
    goTo("http://www.google.com");
                
    assertPresenceOf(link("Images"));
    assertPresenceOf(atLeast(4), links().with(text(not(equalTo("Images")))));
                    
    clickOn(link("Images"));
                
    assertPresenceOf(title().with(text(equalTo("Google Image Search"))));
  }

}