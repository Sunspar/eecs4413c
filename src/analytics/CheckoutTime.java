package analytics;

import java.math.BigDecimal;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
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
    	HttpSession sn = arg.getSession();
    	ServletContext context = sn.getServletContext();
    	
    	final String cxCheckout = "totalCheckoutAvg";
    	final String cxCart = "totalCartAvg";
    	
    	final String cxCheckCount = "checkOutCount";
    	final String cxCartCount = "cartCount";
    			
    	if ( arg.getName().equals("checkout") ) {
    		long creation = sn.getCreationTime();
    		long newCheckoutTime = (long) arg.getValue() - creation;
	    	
    		System.out.println("[LISTENER] CheckoutTime.java: \tAttribute added" + arg.getName() + "\n\t" +
	    			"New time: " + newCheckoutTime   + "\n\t" +
    				"Creation time: " + creation  + "\n\t" + 
	    			"Actual val: " + arg.getValue());
	    	// Check to see if there is already a running, else it's the first average
	    	double totalCheckoutAvg;
			if ( (context.getAttribute(cxCheckout)) != null 
	    			&& ( ( totalCheckoutAvg = (double) context.getAttribute(cxCheckout) ) >= 0) ) 
			{
				totalCheckoutAvg = (double) (totalCheckoutAvg + newCheckoutTime) / 2.0; 
				context.setAttribute(cxCheckout, totalCheckoutAvg);
	    	} else {
	    		context.setAttribute(cxCheckout, newCheckoutTime);
	    	}
			
    	} else if ( arg.getName().equals("lastAddCart") ) {
    		long creation = sn.getCreationTime();
    		long newCartTime = (long) arg.getValue() - creation;
    		
    		System.out.println("[LISTENER] CheckoutTime.java: \tAttribute added" + arg.getName() + "\n\t" +
	    			"New time: " + newCartTime   + "\n\t" +
    				"Creation time: " + creation  + "\n\t" + 
	    			"Actual val: " + arg.getValue());
    		//sn.setAttribute("addCartAvg", (long) arg.getValue() - sn.getCreationTime());
    		
    		// Check to see if there is already a running, else it's the first average
	    	long totalCartAvg;
			if ( (context.getAttribute(cxCart)) != null 
	    			&& ( ( totalCartAvg = (long) context.getAttribute(cxCart) ) >= 0) ) 
			{
				totalCartAvg = (totalCartAvg + newCartTime) / 2; 
				context.setAttribute(cxCart, totalCartAvg);
	    	} else {
	    		context.setAttribute(cxCart, newCartTime);
	    		context.setAttribute(cxCheckCount, 1L);
	    	}
			//sn.setAttribute("addCartAvg", newCartTime);
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
    	HttpSession sn = arg.getSession();
    	ServletContext context = sn.getServletContext();
    	
    	final String cxCheckout = "totalCheckoutAvg";
    	final String cxCart = "totalCartAvg";
    	
    	final String cxCheckCount = "checkOutCount";
    	final String cxCartCount = "cartCount";
    	
    	// arg is old value of session that was just replaced
    	if (arg.getName().equals("checkout")) {
    		
    		// T(n+1) = Tn(n/n+1) + t(1/n+1)
    		
    		System.out.println("[LISTENER] CheckoutTime.java: \tNew checkout" + "\n\t" +
	    			"Last checkout: " + (long) sn.getAttribute("checkout"));
	    	System.out.println("[LISTENER] Total Checkout time: \tAttribute added" + arg.getName() + "\n\t" +
	    			"Checkout time: " + ((long) sn.getAttribute("checkout") - (long) arg.getValue()) );
	    	
	    	long newTime = (long) sn.getAttribute("checkout") - (long) arg.getValue();
	    	
	    	double avg = (double) context.getAttribute(cxCheckout);
	    	avg = (avg + newTime) / 2;
	    	
	    	context.setAttribute(cxCheckout, avg);
    	}
    	else if (arg.getName().equals("lastAddCart")) {
    		/* {
	    	System.out.println("[LISTENER] Add cart time:" + arg.getValue());
	    	System.out.println("[LISTENER] Last time:" + lastTime);
	    	System.out.println("[LISTENER] New time:" + newTime);
	    	System.out.println("[LISTENER] Average time:" + average);
    		} */
    		
    		System.out.println("DEBUG: " + context.getAttribute(cxCart));
    		
    		long newTime = (long) sn.getAttribute("lastAddCart") - (long) arg.getValue();
	    	long avg = (long) context.getAttribute(cxCart);
	    	long count = (long) context.getAttribute(cxCheckCount);
	    	
	    	// formula to calculate running average
	    	avg = avg + ( (newTime - avg)/(count+1) );
	    	
	    	System.out.println("[LISTENER] New time:" + newTime);
	    	System.out.println("[LISTENER] Average time:" + avg);
	    	
	    	context.setAttribute(cxCart, avg);
    	} 
    }
	
}
