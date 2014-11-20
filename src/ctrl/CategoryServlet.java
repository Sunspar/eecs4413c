package ctrl;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.DAO;

/**
 * Servlet implementation class StartB
 */
public class CategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CategoryServlet() {
        super();
        System.out.println("B-init");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String target = "/Front.jspx";
		
		String id = (String) request.getParameter("id");
		System.out.println("[CategoryServlet.java line 37]: " + id);
		
		ServletContext ctx = getServletContext();
		Properties props = (Properties) ctx.getAttribute(ctx.getInitParameter("PROPERTIES"));
		DAO mDAO = (DAO) getServletContext().getAttribute(props.getProperty("INTERNAL_DAO"));
		
		try {
			request.setAttribute("itemsByCat", mDAO.getItemsByCategory(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		request.setAttribute("target", "Category.jspx");
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
