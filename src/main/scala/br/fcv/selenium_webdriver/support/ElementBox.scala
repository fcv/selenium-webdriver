package br.fcv.selenium_webdriver.support

import org.openqa.selenium.WebElement
import org.openqa.selenium.{ NoSuchElementException => NoSuchWebElementException }
import org.openqa.selenium.By

import scala.collection.JavaConversions._

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
}

final case class Empty(path: List[WebElement], by: By) extends ElementBox(path) {
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

final case class Full(path: List[WebElement], current: WebElement) extends ElementBox(path :+ current) {
    def get = current
    val isEmpty = false
}