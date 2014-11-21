package ctrl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.DAO;
import model.ShoppingCart;
import model.ShoppingCartItem;

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
		}
		
		// Update the cart prices on whatever is left in the cart. MUST be done to show the user the most 
		// up-to-date price for everything they order
		try {
			for (int idx = 0; idx < cart.size(); idx ++) {
				ShoppingCartItem item = cart.getItem(idx);
				item.setPrice(dao.getItemPrice(item.getItemName()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		request.setAttribute(props.getProperty("CART_CONTENTS"), cart.getCartContents());
		request.setAttribute("target", "/Cart.jspx");
		request.getRequestDispatcher("/Front.jspx").forward(request, response);
	}

}
