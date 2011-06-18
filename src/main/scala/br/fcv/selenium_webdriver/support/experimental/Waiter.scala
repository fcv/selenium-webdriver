package br.fcv.selenium_webdriver.support.experimental
import org.openqa.selenium.TimeoutException

/**
 * Experimental replacement for WebDriverWait
 * 
 */
class Waiter(val checkPeriod: Long, val timeout: Long) {
    
    /**
     * 
     * @throws org.openqa.selenium.TimeoutException
     * @throws java.lang.InterruptedException
     */
    def until[T](check: => Option[T]): T = {
        
        val now: Long = System.currentTimeMillis
        val maximumTime = now + timeout
        
        var option: Option[T] = None
        while (option.isEmpty && System.currentTimeMillis < maximumTime) {
            option = check            
            
            if (option isEmpty)
                sleep
        }
        
        option match {
            case Some(result) => result
            case None => throwTimeoutException            
        }
        
    }
    
    protected def throwTimeoutException(): Nothing = {
        throw new TimeoutException("Operation timed out after waiting %s miliseconds".format(timeout))
    }
    
    protected def sleep: Unit = {
        Thread.sleep(checkPeriod)
    }
    

}
