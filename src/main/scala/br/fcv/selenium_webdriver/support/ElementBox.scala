package br.fcv.selenium_webdriver.support

import org.openqa.selenium.WebElement
import org.openqa.selenium.{ NoSuchElementException => NoSuchWebElementException }
import org.openqa.selenium.By

import scala.collection.JavaConversions._

/** 
 * Abstraction very similar to [[scala.Option]] but this implementation
 * holds a reference to all known previously used <code>WebElement</code>s so
 * that better error message could be shown when an <code>NoSuchElementException</code>
 * is throw. Ex: 
 * 
 * {{{
 * val elBox: ElementBox = driver \ By.id("my-id") \ By.name("my-name") \ By.id("unkown-id")
 * val el: WebElement = elBox.get //- NoSuchElementException thrown 
 * }}}
 * 
 * In the example above <code>NoSuchElementException</code> will carry a message saying 
 * that <code>unkown-id</code> was not found under <code>\ By.id("my-id") \ By.name("my-name")</code> instead of 
 * only saying that <code>unkown-id</code> was not found.
 * 
 */
sealed abstract class ElementBox(path: List[WebElement]) {
    
    def get: WebElement
    def isEmpty: Boolean

    def ! = get

    def \(by: By) = {
        if (isEmpty) { 
        	this
        } else {
	        try {
	        	Full( path, get findElement by )
	        } catch {
	            case e: NoSuchWebElementException => Empty(path, by)
	        }
    	}
    }
    
    def \\ (by: By): List[WebElement] = if (isEmpty) Nil else List(get findElements by: _*)
    
    def foreach[U](f: WebElement => U) = if (!isEmpty) f(get)
    
    def getOrElse[B >: WebElement](default: => B) = if (isEmpty) default else get
    
    def map(f: WebElement => WebElement): ElementBox = if (isEmpty) this else Full(path, f(get))
    
    def filter(predicate: WebElement => Boolean): ElementBox = if (!isEmpty && predicate(get)) this else Empty(path, null)
    
    def flatMap(f: WebElement => ElementBox): ElementBox = if (isEmpty) this else f(get)    
    
}

/**
 * Represents an empty [[br.fcv.selenium_webdriver.support.ElementBox]]
 * 
 * Note that every access to <code>get</code> method will throw <code>org.openqa.selenium.NoSuchElementException</code>
 */
final class Empty(path: List[WebElement], by: By) extends ElementBox(path) {
    def get = throw new NoSuchWebElementException( (path map printWebElement).mkString("\\") + " \\ " + by )
    val isEmpty = true
    
    private def printWebElement(elm: WebElement): String = {
        val builder = StringBuilder.newBuilder += '<' ++= elm.getTagName;        
        List("id", "name") foreach { attName => 
        	val att = elm.getAttribute(attName)
	        if (att != null && att.length > 0 ) {
	            builder += ' ' ++= attName ++= "=\"" ++= att += '\"'
	        }
        }
        builder ++= " .. >"
        builder.toString   
    }
}

object Empty {    
    def apply(path: List[WebElement], by: By) = new Empty(path, by)
    def unapply(e: Empty) = true
}

/**
 * An [[br.fcv.selenium_webdriver.support.ElementBox]] which holds an WebElement.
 */
final class Full(path: List[WebElement], current: WebElement) extends ElementBox(path :+ current) {
    def get = current
    val isEmpty = false
}

object Full {
    def apply(path: List[WebElement], current: WebElement) = new Full(path, current)
    def unapply(f: Full) = true
}