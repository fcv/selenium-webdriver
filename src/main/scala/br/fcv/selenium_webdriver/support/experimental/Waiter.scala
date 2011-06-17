package br.fcv.selenium_webdriver.support.experimental

/**
 * Experimental replacement for WebDriverWait
 * 
 */
class Waiter(val checkPeriod: Int, val timeout: Int) {
    
    def until[T](check: => Option[T]): T = {
        val now: Long = System.currentTimeMillis
        val maximumTime = now + timeout
        
        while (System.currentTimeMillis < maximumTime) {
            val result = check
            
            
        }
        
        throw new UnsupportedOperationException
    }

}