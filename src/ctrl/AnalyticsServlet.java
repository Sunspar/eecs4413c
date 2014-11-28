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
public class AnalyticsServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public AnalyticsServlet() {
    super();
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String target = "/Front.jspx";

    // System.out.println("[CategoryServlet.java line 37]: " + id);

    ServletContext ctx = getServletContext();
    Properties props =
        (Properties) ctx.getAttribute(ctx.getInitParameter("PROPERTIES"));
    // Product mProduct = (Product)
    // getServletContext().getAttribute(props.getProperty("MAIN_MODEL"));
    request.setAttribute("target", "Analytics.jspx");
    request.setAttribute("totalCheckoutAvg", ctx.getAttribute("totalCheckoutAvg"));
    request.setAttribute("totalCartAvg", ctx.getAttribute("totalCartAvg"));
    RequestDispatcher rd = request.getRequestDispatcher(target);
    rd.forward(request, response);
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    this.doGet(request, response);
  }

}
