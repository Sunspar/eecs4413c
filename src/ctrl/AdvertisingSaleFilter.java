package ctrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
		System.out.println("[AdvertisingSaleFilter] Before doFilter()");
		chain.doFilter(request, response);
		
		Map<String, Set<String>> crossSales = (Map<String, Set<String>>) request.getServletContext().getAttribute("CROSS_SALES_MAP");
		
		Set<String> sales = (Set<String>) crossSales.get("1409S413");
		System.out.println(sales.contains("2002H712"));
		System.out.println(sales.contains("2002H713"));
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
