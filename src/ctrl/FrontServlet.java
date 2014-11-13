package ctrl;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	 * @see Servlet#init()
	 */
	public void init() throws ServletException {
		super.init();
		
		// fuck this
		
		//Load in the properties file
		try {
			Properties properties = new Properties();
			String value = getServletContext().getInitParameter("SHARED_VALUES_FILE");
			InputStream streamData = getServletContext().getResourceAsStream(value);
			properties.load(streamData);
			
			getServletContext().setAttribute(
					getServletContext().getInitParameter("PROPERTIES"),
					properties	
			);
			
		} catch (IOException e) {
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
		String testVal = props.getProperty("FD_TEST_VAL");
		
		
		
	}

}
