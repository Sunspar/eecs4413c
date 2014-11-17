package ctrl;

import model.DAO;

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
import java.util.Properties;

@WebServlet(
		name = "Front Servlet for Foods R Us",
		urlPatterns = "/eFoods"
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
		
		// this is a test addition

	}

}
