package ctrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import model.CustomerBean;
import model.ItemBean;
import model.Product;
import model.ShoppingCart;
import model.ShoppingCartItem;

/**
 * Servlet Filter implementation class AdvertisingSaleFilter
 */
@WebFilter(dispatcherTypes = {
				DispatcherType.REQUEST, 
				DispatcherType.FORWARD
		}
					, servletNames = { "ProductServlet" })
public class AdvertisingSaleFilter implements Filter {
    public AdvertisingSaleFilter() {}

	public void destroy() {}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		ServletContext ctx = request.getServletContext();
		HttpSession session = ((HttpServletRequest) request).getSession();
		Properties props = (Properties) ctx.getAttribute(ctx.getInitParameter("PROPERTIES"));
		Product mProduct = (Product) ctx.getAttribute(props.getProperty("MAIN_MODEL"));
		Map<String, Set<String>> crossSales = (Map<String, Set<String>>) ctx.getAttribute("CROSS_SALES_MAP");

		// Parse out the cart items, and build the user's specific cross-sale list
		ShoppingCart userCart = (ShoppingCart) session.getAttribute(props.getProperty("INTERNAL_CART"));
		
		// Ensure we have a valid cart -- use session variable, or make one if it doesn't exist yet
		userCart = (ShoppingCart) session.getAttribute(props.getProperty("INTERNAL_CART"));
		if (userCart == null) {
			userCart = new ShoppingCart();
			session.setAttribute(props.getProperty("INTERNAL_CART"), userCart);
		}
		
		List<ShoppingCartItem> userCartContents = userCart.getCartContents();
		List<ItemBean> crossSaleItems = new ArrayList<ItemBean>();
		
		try {
			for (ShoppingCartItem item : userCartContents) {
				if (crossSales.containsKey(item.getNumber())) {
					Set<String> crossSaleIDsToAdd = crossSales.get(item.getNumber());
					for (String id : crossSaleIDsToAdd) {
						crossSaleItems.add(mProduct.getItem(id));
					}
				}
			}
		} catch (Exception e) {
			throw new ServletException("Issue contacting database.");
		}
		
		request.setAttribute(props.getProperty("CROSS_SALE_ITEM_LIST"), crossSaleItems);
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		try {
			Map<String, Set<String>> crossSales = new HashMap<String, Set<String>>();
			ServletContext ctx = fConfig.getServletContext();
			String value = ctx.getInitParameter("SALES_MAP_FILE");
			InputStream streamData = ctx.getResourceAsStream(value);
			BufferedReader reader = new BufferedReader(new InputStreamReader(streamData));
			String line = null;
			StringTokenizer keyValueTokenizer = null;
			StringTokenizer valueSetTokenizer = null;
			
			while ((line = reader.readLine()) != null) {
				keyValueTokenizer = new StringTokenizer(line, "=");
				String key = keyValueTokenizer.nextToken();
				String valueSetString = keyValueTokenizer.nextToken();
				valueSetTokenizer = new StringTokenizer(valueSetString, ",");
				Set<String> valueSet = new HashSet<String>();
				
				while (valueSetTokenizer.hasMoreTokens()) {
					valueSet.add(valueSetTokenizer.nextToken());
				}
				
				crossSales.put(key, valueSet);
			}
			
			ctx.setAttribute("CROSS_SALES_MAP", crossSales);
		} catch (IOException e) {
			throw new ServletException(e.getMessage(), e.getCause());
		}
	}
}
