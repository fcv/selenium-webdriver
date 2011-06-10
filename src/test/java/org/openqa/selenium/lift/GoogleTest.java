package org.openqa.selenium.lift;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.openqa.selenium.lift.Finders.link;
import static org.openqa.selenium.lift.Finders.links;
import static org.openqa.selenium.lift.Finders.title;
import static org.openqa.selenium.lift.Matchers.atLeast;
import static org.openqa.selenium.lift.Matchers.text;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

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