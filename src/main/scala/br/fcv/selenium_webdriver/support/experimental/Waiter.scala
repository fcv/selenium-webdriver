package br.fcv.selenium_webdriver.support.experimental

import org.openqa.selenium.{TimeoutException, WebElement}
import br.fcv.selenium_webdriver.support.ElementBox

/**
 * Experimental replacement for WebDriverWait
 * 
 */
class Waiter(val checkPeriod: Long, val timeout: Long) {
    
    private trait InternalResult[+T] {
        def hasResult: Boolean
        def extractResult: T
    }
    
    private object NoResult extends InternalResult[Nothing] {
        override val hasResult = false
        override def extractResult = throw new IllegalStateException("NoResult")
    }
    
    /**
     * Waits until check function returns [[br.fcv.selenium_webdriver.support.Full]] 
     * 
     * @throws org.openqa.selenium.TimeoutException
     * @throws java.lang.InterruptedException
     */
    def until(check: => ElementBox): WebElement = untilImpl {
        val elm = check
        new InternalResult[WebElement] {
            override val hasResult = !elm.isEmpty
            override def extractResult = elm.get
        }
    }
    
    /**
     * Waits until check function returns <code>true</code> 
     * 
     * @throws org.openqa.selenium.TimeoutException
     * @throws java.lang.InterruptedException
     */
    def until(check: => Boolean): Unit = untilImpl {
        val bool = check
        new InternalResult[Unit] {
            override val hasResult = bool
            override def extractResult = ()
        }
    }
    
    /**
     * Waits until check function returns [[scala.Some]]
     * 
     * @throws org.openqa.selenium.TimeoutException
     * @throws java.lang.InterruptedException
     */
    def until[T](check: => Option[T]): T = untilImpl {
        val opt = check
        new InternalResult[T] {
            override val hasResult = opt.isDefined
            override def extractResult = opt.get
        }
    }
    
    private def untilImpl[T](check: => InternalResult[T]): T = {
        
        val now: Long = System.currentTimeMillis
        val maximumTime = now + timeout
        
        var option: InternalResult[T] = NoResult
        while (!option.hasResult && System.currentTimeMillis < maximumTime) {
            option = check            
            
            if (!option.hasResult)
                sleep
        }
        
        if (option.hasResult)
            option.extractResult
        else 
            throwTimeoutException
    }
    
    protected def throwTimeoutException(): Nothing = {
        throw new TimeoutException("Operation timed out after waiting %s miliseconds".format(timeout))
    }
    
    protected def sleep: Unit = {
        Thread.sleep(checkPeriod)
    }
    
}

object Waiter {
    object TimeImplicits {
        
        implicit def enrichLong(i: Long) = new {
            def miliseconds = i
            def seconds = i * 1000
            def minutes = 1 * 1000 * 60
        }
        
    }
}
