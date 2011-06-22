package br.fcv.selenium_webdriver.support.experimental

import org.openqa.selenium.{TimeoutException, WebElement}
import br.fcv.selenium_webdriver.support.ElementBox

/**
 * Experimental replacement for WebDriverWait.
 * 
 * Main motivation points are:
 * <ul>
 * 	<li>Misleading name. WebDriverWait is not a WebDriver</li>
 * 
 * <li>Don't see why check function receives a WebDriver instance as parameter. It is normally accessible as a outer variable.</li>
 * 
 * <li>Rules for result of check function are confusing </li>
 * 
 * <li>Make use of concise scala syntax </li>
 * </ul>
 * 
 * Usage example:
 * 
 * {{{
 * val driver = createWebDriver()
 * val wait = new Waiter(checkPeriod = 50, timeout = 2000)
 * 
 * //- if implicts converstion are available .. returning ElementBox
 * val myId: WebElement = wait until { driver \ By.id("my-id") }
 * myId.click
 * 
 * //- or using orignal webdriver methods .. returning Option[WebElement] 
 * val anotherId: WebElement = wait until {
 *   // note that unlike WebDriverWait this implementation does not swallow any exception
 *   try {
 *   	Some( driver findElement By.id("another-id") )
 *   } catch {
 *   	case e: NoSuchElementException => None
 *   }
 * }
 * anotherId.click
 * 
 * // .. returning boolean
 * wait until { (driver findElements By.className("classname")).size > 4 }
 * // there are more than 4 'classname' elements 
 * }}} 
 * 
 */
class Waiter(val checkPeriod: Long, val timeout: Long) {
    
    /**
     * Adapter type created so several overload of until method 
     * can use same background implementation: untilImpl
     */
    private trait InternalResult[+T] {
        def hasResult: Boolean
        def extractResult: T
    }
    
    /**
     * Empty instance of InternalResult. It is for InternalResult as [[scala.None]] is for 
     * [[scala.Option]] and [[scala.Nil]] is for [[scala.List]].
     */
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
        
        import System.currentTimeMillis
        val maximumTime = currentTimeMillis + timeout
        
        var option: InternalResult[T] = NoResult
        //-- repeat while check has not a success result and timeout has not been reached
        while (!option.hasResult && currentTimeMillis < maximumTime) {
            option = check            
            
            if (!option.hasResult) {
                val now = currentTimeMillis
                //-- verify if current time more check period won't be before timeout 
                val sleepTime = if (now + checkPeriod < maximumTime) {
	                    checkPeriod
	                } else {
	                    //-- if so, just use remaining time.. not entire period
	                	maximumTime - now
	                }
                
                if (sleepTime >= 0)
                	Thread.sleep(sleepTime)
            }
        }
        
        if (option.hasResult)
            option.extractResult
        else 
            throwTimeoutException
    }
    
    protected def throwTimeoutException(): Nothing = {
        throw new TimeoutException("Operation timed out after waiting %s miliseconds".format(timeout))
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
