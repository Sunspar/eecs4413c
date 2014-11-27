package ctrl;

import model.DAO;
import model.Product;
import model.ShoppingCart;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

@WebServlet(
		name = "Front Servlet for Foods R Us",
		urlPatterns = { "/e/*", "/eFoods", "/eFoods/*" }
)
public class FrontServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FrontServlet() {
        super();
    }

	/**
	 * @see javax.servlet.Servlet#init()
	 */
    @Override
	public void init() throws ServletException {
		super.init();
		
		//Load in the properties file
		try {
			Properties properties = new Properties();
			String value = getServletContext().getInitParameter("SHARED_VALUES_FILE");
			InputStream streamData = getServletContext().getResourceAsStream(value);
			properties.load(streamData);

            // Save the properties object for various servlets to use
			getServletContext().setAttribute(
					getServletContext().getInitParameter("PROPERTIES"),
					properties	
			);
 
            // Save main product model
            getServletContext().setAttribute(
                    properties.getProperty("MAIN_MODEL"),
                    new Product()
            );
			
		} catch (IOException | NamingException e) {
			throw new ServletException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext ctx = getServletContext();
		HttpSession session = request.getSession();
		String targetPage = ""; // WE DONT KNOW YET GOSH.
		Properties props = (Properties) ctx.getAttribute(ctx.getInitParameter("PROPERTIES"));
		Product mProduct = (Product) getServletContext().getAttribute(props.getProperty("MAIN_MODEL"));
		
		try {
			request.setAttribute("categories", mProduct.getAllCategories());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (session.getAttribute("name") != null){
			System.out.println(session.getAttribute("name"));
			request.setAttribute("name", session.getAttribute("name"));
		}

		/* Cart dispatcher */
		if ((request.getPathInfo() != null && request.getPathInfo().equals("/Search")) 
				|| (request.getPathInfo() != null && request.getPathInfo().equals("/Category")))
		{
			request.setAttribute("ticket", "F-to-Cart");
			this.getServletContext().getNamedDispatcher("ProductServlet").forward(request, response);
		} 
		else if (request.getPathInfo() != null && request.getPathInfo().equals("/Cart"))
		{
			request.setAttribute("ticket", "F-to-Cart");
			this.getServletContext().getNamedDispatcher("ShoppingCartServlet").forward(request, response);
		} 
		else if (request.getPathInfo() != null && request.getPathInfo().equals("/Login")) {
			request.setAttribute("ticket", "F-to-Login");
			ctx.getNamedDispatcher("LoginServlet").forward(request, response);
		} 
		else if (request.getPathInfo() != null && request.getPathInfo().equals("/Analytics")) {
			request.setAttribute("ticket", "F-to-Login");
			ctx.getNamedDispatcher("Analytics").forward(request, response);
		} 
		else
		{
			request.setAttribute("ticket", "Front");
			request.getRequestDispatcher("/Front.jspx").forward(request, response);
		}

	}

}
