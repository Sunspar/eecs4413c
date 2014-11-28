package ctrl;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class StartB
 */
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String target = "/Front.jspx";
		
		ServletContext ctx = getServletContext();
		Properties props = (Properties) ctx.getAttribute(ctx.getInitParameter("PROPERTIES"));
		HttpSession session = request.getSession();
		/* remove any identifier associated with user */
		if (session.getAttribute(props.getProperty("INTERNAL_CUSTOMER")) != null) {
			session.removeAttribute(props.getProperty("INTERNAL_CUSTOMER"));
		}
		if (session.getAttribute(props.getProperty("cse")) != null ) {
			session.removeAttribute("cse");
		}
		if (session.getAttribute("name") != null){
			session.removeAttribute("name");
			request.removeAttribute("name");
		}
		
		request.getRequestDispatcher(target).forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
