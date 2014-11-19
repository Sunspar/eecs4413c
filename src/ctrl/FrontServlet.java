package ctrl;

import model.DAO;
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
		urlPatterns = { "/eFoods", "/eFoods/*" }
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

            // Save the DAO for various servlets to use
            getServletContext().setAttribute(
                    properties.getProperty("INTERNAL_DAO"),
                    new DAO()
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext ctx = getServletContext();
		HttpSession session = request.getSession();
		String targetPage = ""; // WE DONT KNOW YET GOSH.
		Properties props = (Properties) ctx.getAttribute(ctx.getInitParameter("PROPERTIES"));
		String testVal = props.getProperty("INTERNAL_DAO");
		
		// Debug info at the start of front servlet
		System.out.println("In Front--begin: *****************");
		System.out.println("url = " + request.getRequestURL());
		System.out.println("uri = " + request.getRequestURI());
		System.out.println("pth = " + request.getPathInfo());
		System.out.println("In Front--finish *****************\n");
		System.out.println("[DEBUG] FrontServlet: ShoppingCart obj is " + session.getAttribute(props.getProperty("INTERNAL_CART")));
		
		// If the user doesnt have a cart, initialize one for them.
		if (session.getAttribute(props.getProperty("INTERNAL_CART")) == null) {
			session.setAttribute(props.getProperty("INTERNAL_CART"), new ShoppingCart());
		}
		
		
		
		if (request.getPathInfo() != null && request.getPathInfo().equals("/A"))
		{
			request.setAttribute("ticket", "F-to-A");
			this.getServletContext().getNamedDispatcher("A").forward(request, response);
		} 
		else if (request.getPathInfo() != null && request.getPathInfo().equals("/B"))
		{
			request.setAttribute("ticket", "F-to-B");
			this.getServletContext().getNamedDispatcher("B").forward(request, response);
		} 
		else
		{
			DAO mDAO = (DAO) getServletContext().getAttribute(props.getProperty("INTERNAL_DAO"));
			
			request.setAttribute("ticket", "Front");

			try {
				request.setAttribute("categories", mDAO.getAllCategories());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.getRequestDispatcher("/Front.jspx").forward(request, response);
		}

	}

}
