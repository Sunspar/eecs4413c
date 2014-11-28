package ctrl;

import java.io.File;
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

import model.CustomerBean;
import model.DAO;
import model.ItemBean;
import model.Product;
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
		boolean isLoginNeeded = false; // Triggers a forced login on exiting the cart, if the user is not logged in
		boolean isPurchaseOrderCreated = false; // Set if user finalizes an order
		
		ServletContext ctx = getServletContext();
		Properties props = (Properties) ctx.getAttribute(ctx.getInitParameter("PROPERTIES"));
		HttpSession session = request.getSession();
		
		Product mProduct = (Product) ctx.getAttribute(props.getProperty("MAIN_MODEL"));
		
		// Ensure we have a valid cart -- use session variable, or make one if it doesn't exist yet
		ShoppingCart cart = (ShoppingCart) session.getAttribute(props.getProperty("INTERNAL_CART"));
		if (cart == null) {
			cart = new ShoppingCart();
			session.setAttribute(props.getProperty("INTERNAL_CART"), cart);
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
			CustomerBean customer = (CustomerBean) session.getAttribute(props.getProperty("INTERNAL_CUSTOMER"));
			
			// If the customer doesn't exist, then force them to login
			if (customer == null) {
				isLoginNeeded = true;
			} else {
				try {
					String rootFolder = ctx.getRealPath(props.getProperty("SC_PO_ROOT_FOLDER"));
					String poFilename = getPOFilename(rootFolder, customer.getAccount());
					long orderId = getOrderNumber(rootFolder);
					ShoppingCartWrapper wrapper = new ShoppingCartWrapper(customer, orderId, cart.getCartContents());
					
					JAXBContext jc = JAXBContext.newInstance(wrapper.getClass());
					Marshaller marshaller = jc.createMarshaller();
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
					
					StringWriter sw = new StringWriter();
					sw.write("\n");
					marshaller.marshal(wrapper, new StreamResult(sw));
					  
					System.out.println(sw.toString()); // for debugging
					
					String poFileLocation = rootFolder + File.separator + poFilename;
					FileWriter fw = new FileWriter(poFileLocation);
					fw.write("<?xml version='1.0'?>");
					fw.write("<?xml-stylesheet type='text/xsl' href='PO.xsl'?>");
					fw.write(sw.toString());
					fw.close();
					
					cart.empty();
					String webFileLocation = "purchases/" + poFilename;
					request.setAttribute("purchaseOrderXmlFile", webFileLocation);
					isPurchaseOrderCreated = true;
				} catch (JAXBException e) {
					throw new ServletException("Error parsing XML for checkout!");
				}
				
				// Add checkout attribute to HTTPSession for analytics
				session.setAttribute("checkout", System.currentTimeMillis());
				// If session is untouched for 5 mins, count as fresh visit -- should be placed some place more general
				session.setMaxInactiveInterval(300);
			}
		/* FOR AJAX */	
		} else if ((request.getParameter("addName") != null) && (request.getParameter("addID") != null) ) {
			String itemName = request.getParameter("addName");
			String itemID = request.getParameter("addID");
			isExternalAction = true;
			
			// Server response
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			
			try {
				ItemBean item = mProduct.getItem(itemID);
				cart.addItemToCart(item.getName(), item.getNumber(), "1");
				System.out.println("[ShoppingCartServlet]: Reached!");
				out.write("Server says: add successful\nTo add:" + itemName);
				// add to session what time last item was added
				session.setAttribute("lastAddCart", System.currentTimeMillis());
				session.setAttribute("lastAddCartNum", itemName);
			} catch (Exception e) {
				//e.printStackTrace();
				out.write("Server says: add failed\nNo such item:" + itemName);
			}
		}
		
		// Update the cart prices on whatever is left in the cart. MUST be done to show the user the most 
		// up-to-date price for everything they order
		try {
			for (int idx = 0; idx < cart.size(); idx ++) {
				ShoppingCartItem item = cart.getItem(idx);
				item.setPrice(mProduct.getItemPrice(item.getName()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (isLoginNeeded) {
			request.setAttribute("target", "/Login.jspx");
			request.getRequestDispatcher("/Front.jspx").forward(request, response);
		} else if (!isExternalAction) {
			if (isPurchaseOrderCreated) {
				request.setAttribute("target", "/OrderComplete.jspx");
			} else {
				request.setAttribute(props.getProperty("CART_CONTENTS"), cart.getCartContents());
				request.setAttribute("target", "/Cart.jspx");
			}
			
			request.getRequestDispatcher("/Front.jspx").forward(request, response);
		}
	}

	private String getPOFilename(String poFolder, String accountID) {
		File[] listing = new File(poFolder).listFiles();
		if (listing == null) {
			return "po" + accountID + "_01.xml";
		}
		int personalOrderNumber = 1;
		
		for (File item : listing) {
			if (item.getName().contains(accountID)) {
				personalOrderNumber++;
			}
		}
		String result = "po"+ accountID;
		if (personalOrderNumber < 10) {
			result += "_0"+ personalOrderNumber;
		} else {
			result += "_" + personalOrderNumber;
		}
		result += ".xml";
		return result;
		
	}
	
	private long getOrderNumber(String poFolder) {
		File[] listing = new File(poFolder).listFiles();
		return listing.length + 1;
	}
}
