package br.fcv.selenium_webdriver.support

import org.openqa.selenium.WebElement
import org.openqa.selenium.{ NoSuchElementException => NoSuchWebElementException }
import org.openqa.selenium.By
import scala.collection.JavaConversions._
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Builder

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
    self =>
    
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
    
    def map[T, That](f: WebElement => T)(implicit bf: CanBuildFrom[ElementBox, T, That]): That = {
        val b = bf(this)
        b.sizeHint( if (isEmpty) 0 else 1 )
        for (x <- this) b += f(x)
        b.result
    }
    
    def filter(predicate: WebElement => Boolean): ElementBox = if (!isEmpty && predicate(get)) this else Empty(path, null)
    
    def withFilter(predicate: WebElement => Boolean): WithFilter = new WithFilter(predicate)
    
    class WithFilter(p: WebElement => Boolean) {
    	def map[T, That](f: WebElement => T)(implicit bf: CanBuildFrom[ElementBox, T, That]): That = self.filter(p).map(f)(bf)
    	def flatMap(f: WebElement => ElementBox): ElementBox = self filter p flatMap f
    	def foreach[U](f: WebElement => U): Unit = self filter p foreach f
    	def withFilter(q: WebElement => Boolean): WithFilter = new WithFilter(x => p(x) && q(x))
    }
    
    def flatMap(f: WebElement => ElementBox): ElementBox = if (isEmpty) this else f(get)
    
    def toOption = if (isEmpty) None else Some(get)
    
    def toList = if (isEmpty) List(get) else Nil
    
}

object ElementBox extends LowerPriorityImplicits {
    private def newElementBoxBuilder: Builder[WebElement, ElementBox] = new ArrayBuffer[WebElement] mapResult { 
        seq => if (seq.isEmpty) Empty(Nil, null) else Full(Nil, seq.head)
    }
    
    implicit def toElementBox: CanBuildFrom[ElementBox, WebElement, ElementBox] = new CanBuildFrom[ElementBox, WebElement, ElementBox] {
    	def apply = newElementBoxBuilder
    	def apply(elmBox: ElementBox) = newElementBoxBuilder
    }
    
    implicit def toIterable(box: ElementBox): Iterable[WebElement] = box.toList
}

trait LowerPriorityImplicits {
	private def newOptionBoxBuilder[T]: Builder[T, Option[T]] = new ArrayBuffer[T] mapResult {
        seq => if (seq.isEmpty) None else Some(seq.head)
    }
  
	implicit def toOption[T]: CanBuildFrom[ElementBox, T, Option[T]] = new CanBuildFrom[ElementBox, T, Option[T]] {
        def apply = newOptionBoxBuilder[T]
        def apply(elmBox: ElementBox) = newOptionBoxBuilder[T] 
    }
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