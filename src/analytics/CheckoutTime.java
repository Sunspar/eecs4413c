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
  public void attributeAdded(HttpSessionBindingEvent arg) {
    HttpSession sn = arg.getSession();
    ServletContext context = sn.getServletContext();

    // Some final strings named accordingly to Context attributes
    final String cxCheckout = "totalCheckoutAvg";
    final String cxCart = "totalCartAvg";
    final String cxCheckCount = "checkOutCount";
    final String cxCartCount = "cartCount";
    // Some finals named accordingly to Session attributes
    final String snCart = "lastAddCart";
    final String snCheckout = "checkout";

    // -- Begin check for session attribute -- //
    if (arg.getName().equals(snCheckout)) {
      long creation = sn.getCreationTime();
      long newTime = (long) arg.getValue() - creation;

      System.out
          .println("[LISTENER] CheckoutTime.java: \tAttribute added [Checkout]"
              + arg.getName() + "\n\t" + "New time: " + newTime + "\n\t"
              + "Creation time: " + creation + "\n\t");


      long avg;
      long count;
      // Check to see if there is already a running, else it's the first average
      if ((context.getAttribute(cxCheckout)) != null
          && ((avg = (long) context.getAttribute(cxCheckout)) >= 0)) {
        if ((context.getAttribute(cxCheckCount)) != null
            && ((count = (long) context.getAttribute(cxCheckCount)) >= 0)) {
          // formula to calculate running average
          count++;
          avg = avg + ((newTime - avg) / count);
          // Update context attributes
          context.setAttribute(cxCheckout, avg);
          context.setAttribute(cxCheckCount, count);
        }
      } else {
        // if this is the first time put in the context
        context.setAttribute(cxCheckout, newTime);
        context.setAttribute(cxCheckCount, 1L);
      }
    } else if (arg.getName().equals(snCart)) {
      long creation = sn.getCreationTime();
      long newTime = (long) arg.getValue() - creation;

      System.out.println("[LISTENER] CheckoutTime.java: \tAttribute added"
          + arg.getName() + "\n\t" + "New time: " + newTime + "\n\t"
          + "Creation time: " + creation + "\n\t");

      long avg;
      long count;
      // Check to see if there is already a running, else it's the first average
      if ((context.getAttribute(cxCart)) != null
          && ((avg = (long) context.getAttribute(cxCart)) >= 0)) {
        if ((context.getAttribute(cxCartCount)) != null
            && ((count = (long) context.getAttribute(cxCartCount)) >= 0)) {
          // formula to calculate running average
          count++;
          avg = avg + ((newTime - avg) / count);
          // Update context attributes
          context.setAttribute(cxCart, avg);
          context.setAttribute(cxCartCount, count);
        }
      } else {
        // if this is the first
        context.setAttribute(cxCart, newTime);
        context.setAttribute(cxCartCount, 1L);
      }
    }
    // -- End check for session attribute -- //
  }

  /**
   * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
   */
  public void attributeRemoved(HttpSessionBindingEvent arg) {}

  /**
   * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
   */
  public void attributeReplaced(HttpSessionBindingEvent arg) {
    HttpSession sn = arg.getSession();
    ServletContext context = sn.getServletContext();

    // Some final strings named accordingly to Context attributes
    final String cxCheckout = "totalCheckoutAvg";
    final String cxCart = "totalCartAvg";
    final String cxCheckCount = "checkOutCount";
    final String cxCartCount = "cartCount";
    // Some finals named accordingly to Session attributes
    final String snCart = "lastAddCart";
    final String snCheckout = "checkout";

    // arg is old value of session that was just replaced
    if (arg.getName().equals(snCheckout)) {
      long newTime = (long) sn.getAttribute(snCheckout) - (long) arg.getValue();
      long avg = (long) context.getAttribute(cxCheckout);
      long count = (long) context.getAttribute(cxCheckCount);

      // formula to calculate running average
      count++;
      avg = avg + ((newTime - avg) / count);
      // Update context attributes
      context.setAttribute(cxCheckout, avg);
      context.setAttribute(cxCheckCount, count);
      System.out.println("[LISTENER] New time:" + newTime);
      System.out.println("[LISTENER] Average time:" + avg);
    } else if (arg.getName().equals(snCart)) {
      long newTime = (long) sn.getAttribute(snCart) - (long) arg.getValue();
      long avg = (long) context.getAttribute(cxCart);
      long count = (long) context.getAttribute(cxCartCount);

      // formula to calculate running average
      count++;
      avg = avg + ((newTime - avg) / count);
      // Update context attributes
      context.setAttribute(cxCart, avg);
      context.setAttribute(cxCartCount, count);

      System.out.println("[LISTENER] New time:" + newTime);
      System.out.println("[LISTENER] Average time:" + avg);
    }
  }

}
