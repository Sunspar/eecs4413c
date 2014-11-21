package ctrl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
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
 * Servlet implementation class StartA
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
		
		if (session.getAttribute(props.getProperty("INTERNAL_CART")) == null) {
			session.setAttribute(props.getProperty("INTERNAL_CART"), new ShoppingCart());
		}
		
		
		
		ShoppingCart cart = (ShoppingCart) session.getAttribute(props.getProperty("INTERNAL_CART"));
		
		// Update the cart prices.
		/**
		 * The following code updates the prices in the shopping cart. This MUST be done in order to show the customer
		 * the most up-to-date pricing for items! The price may have changed since they added the item to the cart.
		 */
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
