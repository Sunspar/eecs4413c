package analytics;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * Application Lifecycle Listener implementation class CheckoutTime
 *
 */
@WebListener
public class CheckoutTime implements HttpSessionAttributeListener {
	
    /**
     * Default constructor. 
     */
    public CheckoutTime() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent arg)  { 
    	if ( arg.getName().equals("checkout") ) {
	    	System.out.println("[LISTENER] CheckoutTime.java: \tAttribute added" + arg.getName() + "\n\t" +
	    			"Created: " + arg.getSession().getCreationTime());
	    	System.out.println("[LISTENER] Total Checkout time: \tAttribute added" + arg.getName() + "\n\t" +
	    			"Checkout time: " + (System.currentTimeMillis() - arg.getSession().getCreationTime()) );
    	} else if ( arg.getName().equals("lastAddCart") ) {
    		System.out.println("[LISTENER] CheckoutTime.java: \tAttribute added" + arg.getName() + "\n\t" +
	    			"Value: " + arg.getValue());
    		arg.getSession().setAttribute("addCartAvg", (long) arg.getValue() - arg.getSession().getCreationTime());
    	}
    }

	/**
     * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
     */
    public void attributeRemoved(HttpSessionBindingEvent arg)  { 
    }

	/**
     * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent arg)  {
    	// arg is old value of session that was just replaced
    	if (arg.getName().equals("checkout")) {
    		System.out.println("[LISTENER] CheckoutTime.java: \tNew checkout" + "\n\t" +
	    			"Last checkout: " + (long) arg.getSession().getAttribute("checkout"));
	    	System.out.println("[LISTENER] Total Checkout time: \tAttribute added" + arg.getName() + "\n\t" +
	    			"Checkout time: " + ((long) arg.getSession().getAttribute("checkout") - (long) arg.getValue()) );
    	}
    	else if (arg.getName().equals("lastAddCart")) {
    		// old value just replaced
    		long lastTime = (long) arg.getValue();
    		// new delta time
    		long newTime = (long) arg.getSession().getAttribute("lastAddCart") - lastTime;
    		long lastAvg = (long) arg.getSession().getAttribute("addCartAvg");
    		long average = (lastAvg + newTime) / 2L;
	    	arg.getSession().setAttribute("addCartAvg", average);
	    	System.out.println("[LISTENER] Add cart time:" + arg.getValue());
	    	System.out.println("[LISTENER] Last time:" + lastTime);
	    	System.out.println("[LISTENER] New time:" + newTime);
	    	System.out.println("[LISTENER] Average time:" + average);
    	} 
    	// test
    }
	
}
