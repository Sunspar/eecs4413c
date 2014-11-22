package ctrl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;

import model.DAO;
import model.ShoppingCart;
import model.ShoppingCartItem;
import model.ShoppingCartWrapper;

/**
 * Servlet implementation class ShoppingCartServlet
 */

public class ShoppingCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShoppingCartServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Flag to see if action is external to shopping cart JSP 
		boolean isExternalAction = false;
		
		ServletContext ctx = getServletContext();
		Properties props = (Properties) ctx.getAttribute(ctx.getInitParameter("PROPERTIES"));
		HttpSession session = request.getSession();
		DAO dao = (DAO) ctx.getAttribute(props.getProperty("INTERNAL_DAO"));
		
		// Ensure we have a valid cart -- use session variable, or make one if it doesn't exist yet
		ShoppingCart cart = (ShoppingCart) session.getAttribute(props.getProperty("INTERNAL_CART"));
		if (cart == null) {
			cart = new ShoppingCart();
			session.setAttribute(props.getProperty("INTERNAL_CART"), cart);
			
			//TODO: Remove demo data below once "Add To Cart" is working
			/* ----- DEMO DATA BEGINS -----*/
			/*
			 * Dummy data inserted because I was too lazy to insert via Eclipse's Display view every time
			 * I restarted the server. Should definitely remove this afterwards...
			 */
			cart.addItemToCart("Minced Rib Meat by VX", "1");
			cart.addItemToCart("J0 Chicken Meat", "5");
			cart.addItemToCart("Nuts Ice Cream with Vanilla by RC", "12");
			/* ----- DEMO DATA ENDS ----- */
		}
		
		// If the user asked to update the quantities of an item, update then now
		if (request.getParameter(props.getProperty("SC_UPDATE")) != null) {
			/* 
			 * Values passed form the shopping cart update button follow the form:
			 * itemName[idx]: <String>
			 * itemQuantity[idx]: <Integer>
			 *
			 * these indexes can be parsed out using substring()  and correspond to the indices that the shopping cart 
			 * java object maintains, that is itemName0 and itemQuantity0 correspond to the shopping cart item returned
			 * by ShoppingCart#getItem(0). Furthermore, we have exactly one pair of these name/quantities for every
			 * current object in the shopping cart. Take careful note that updating a quantity to zero (0) should really
			 * remove it from the cart, and  the resulting cart listing sent back will contain one less item than it did
			 * before for every quantity that was set to zero (0).
			 */
			try {
				List<ShoppingCartItem> cartItems = cart.getCartContents();
				for (int idx = 0; idx < cartItems.size(); idx++) {
					String qtyAsString = request.getParameter(props.getProperty("SC_ITEM_QUANTITY") + idx);
					int qty = Integer.parseInt(qtyAsString);
					if (qty == 0) {
						cartItems.remove(idx);
					} else {
						cartItems.get(idx).setQuantity(qty);
					}
				}
			} catch (NumberFormatException e) {
				throw new ServletException("Quantity was not a valid integer!");
			}
		} else if (request.getParameter(props.getProperty("SC_CHECKOUT")) != null) {
			// User is checking out their cart.
			/*
			 * Steps involved in checking out the cart:
			 * 1. Wrap the carts contents using ShoppingCartWrapper.
			 * 2. Marshall XML file for the PO to some predetermined folder.
			 * 3. (for now) : set target so that the user can view their link to the PO
			 */
			ShoppingCartWrapper wrapper = new ShoppingCartWrapper(cart.getCartContents());
			
			try {
				JAXBContext jc = JAXBContext.newInstance(wrapper.getClass());
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
				
				StringWriter sw = new StringWriter();
				sw.write("\n");
				marshaller.marshal(wrapper, new StreamResult(sw));
				  
				System.out.println(sw.toString()); // for debugging
				/*FileWriter fw = new FileWriter("test.xml");
				fw.write("<?xml version='1.0'?>");
				fw.write("<?xml-stylesheet type='text/xsl' href='SIS.xsl'?>");
				fw.write(sw.toString());
				fw.close();*/
			} catch (JAXBException e) {
				throw new ServletException("Error parsing XML for checkout!");
			}
			
		} else if (request.getParameter("add") != null) {
			String item = request.getParameter("add");
			isExternalAction = true;
			
			// Server response
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.write("Server says: add successful\nTo add:" + item);
			
			cart.addItemToCart(item, "1");
			
			System.out.println("[ShoppingCartServlet]: Reached!");
			
			
		}
		
		// Update the cart prices on whatever is left in the cart. MUST be done to show the user the most 
		// up-to-date price for everything they order
		try {
			for (int idx = 0; idx < cart.size(); idx ++) {
				ShoppingCartItem item = cart.getItem(idx);
				item.setPrice(dao.getItemPrice(item.getName()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (!isExternalAction) {
			request.setAttribute(props.getProperty("CART_CONTENTS"), cart.getCartContents());
			request.setAttribute("target", "/Cart.jspx");
			request.getRequestDispatcher("/Front.jspx").forward(request, response);
		}
	}

}
