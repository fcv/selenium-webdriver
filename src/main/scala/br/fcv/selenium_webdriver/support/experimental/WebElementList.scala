package br.fcv.selenium_webdriver.support.experimental

import org.openqa.selenium.{By, WebElement}
import scala.collection.JavaConversions._
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable.{ArrayBuffer, Builder}
import scala.collection.IndexedSeqLike


class WebElementList private (delegate: List[WebElement]) extends IndexedSeq[WebElement] with IndexedSeqLike[WebElement, WebElementList] {    
    def this() = this(Nil)    
    def length = delegate.length    
    def apply(i: Int) = delegate(i)    
    
    override def newBuilder: Builder[WebElement, WebElementList] = WebElementList.newBuilder
    
    def *\ (by: By) = this.flatMap( e => (e findElements by).toList )

}

object WebElementList {
    
    val empty = new WebElementList(Nil)    
    def apply(elms: WebElement*) = new WebElementList(elms.toList)
    def apply() = empty    
    
    def newBuilder: Builder[WebElement, WebElementList] = new ArrayBuffer[WebElement] mapResult { seq => WebElementList(seq.toList: _*)}
    
    implicit def canBuildFrom: CanBuildFrom[WebElementList, WebElement, WebElementList] = new CanBuildFrom[WebElementList, WebElement, WebElementList] {
        def apply() = newBuilder
        def apply(from: WebElementList) = newBuilder
    }
}

