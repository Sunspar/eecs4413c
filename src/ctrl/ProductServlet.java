package ctrl;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Product;

/**
 * Servlet implementation class StartB
 */
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductServlet() {
        super();
        System.out.println("B-init");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String target = "/Front.jspx";
		
		//System.out.println("[CategoryServlet.java line 37]: " + id);
		
		ServletContext ctx = getServletContext();
		Properties props = (Properties) ctx.getAttribute(ctx.getInitParameter("PROPERTIES"));
		//DAO mDAO = (DAO) getServletContext().getAttribute(props.getProperty("INTERNAL_DAO"));
		Product mProduct = (Product) getServletContext().getAttribute("main");
		
		if ( request.getParameter("id") != null ) {
			String id = (String) request.getParameter("id");
			try {
				request.setAttribute("displayType", "category");
				request.setAttribute("itemsByCat", mProduct.getItemsByCategory(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ( request.getParameter("query") != null ) {
			String query = (String) request.getParameter("query");
			try {
				request.setAttribute("displayType", "search");
				request.setAttribute("query", query);
				request.setAttribute("itemsByCat", mProduct.getItemsBySearch(query));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		request.setAttribute("target", "Product.jspx");
		RequestDispatcher rd = request.getRequestDispatcher(target);
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
