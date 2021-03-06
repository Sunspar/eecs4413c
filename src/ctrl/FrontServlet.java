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

@WebServlet(name = "Front Servlet for Foods R Us", urlPatterns = {"/e/*", "/eFoods",
    "/eFoods/*"})
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

    // Load in the properties file
    try {
      Properties properties = new Properties();
      String value = getServletContext().getInitParameter("SHARED_VALUES_FILE");
      InputStream streamData = getServletContext().getResourceAsStream(value);
      properties.load(streamData);

      // Save the properties object for various servlets to use
      getServletContext().setAttribute(
          getServletContext().getInitParameter("PROPERTIES"), properties);

      // Save main product model
      getServletContext().setAttribute(properties.getProperty("MAIN_MODEL"),
          new Product());

    } catch (IOException | NamingException e) {
      throw new ServletException(e.getMessage(), e.getCause());
    }
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    ServletContext ctx = getServletContext();
    HttpSession session = request.getSession();
    String targetPage = ""; // WE DONT KNOW YET GOSH.
    Properties props =
        (Properties) ctx.getAttribute(ctx.getInitParameter("PROPERTIES"));
    Product mProduct =
        (Product) getServletContext().getAttribute(props.getProperty("MAIN_MODEL"));

    try {
      request.setAttribute("categories", mProduct.getAllCategories());
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (session.getAttribute("name") != null) {
      System.out.println(session.getAttribute("name"));
      request.setAttribute("name", session.getAttribute("name"));
    }

    // Create Global user cart
    // Needed for always-ready cart view
    ShoppingCart cart =
        (ShoppingCart) session.getAttribute(props.getProperty("INTERNAL_CART"));
    if (cart == null) {
      System.out.println("[ShoppingCartServlet]: created new cart!");
      cart = new ShoppingCart();
      session.setAttribute(props.getProperty("INTERNAL_CART"), cart);
    }
    if (session.getAttribute("INTERNAL_CART") != null) {
      // Ask model to calculate shipping rate
      request.setAttribute("shipping",
          mProduct.calculateShippingRate(cart.getCartContents()));
      // Ask model to get current tax rate
      request.setAttribute("tax", mProduct.getTaxRate());
      request.setAttribute("cartContents",
          ((ShoppingCart) session.getAttribute("INTERNAL_CART")).getCartContents());
    }

    /* Dispatch the request to the appropriate servlet */
    if (((request.getParameter("AuthenticationRequest") != null) && (request
        .getAttribute("triedLogin") == null))
        || (request.getPathInfo() != null && request.getPathInfo().equals("/Login"))) {
      request.setAttribute("triedLogin", "");
      request.setAttribute("ticket", "F-to-Login");
      ctx.getNamedDispatcher("LoginServlet").forward(request, response);
    } else if ((request.getPathInfo() != null && request.getPathInfo().equals(
        "/Search"))
        || (request.getPathInfo() != null && request.getPathInfo().equals(
            "/Category"))) {
      request.setAttribute("ticket", "F-to-Products");
      this.getServletContext().getNamedDispatcher("ProductServlet")
          .forward(request, response);
    } else if (request.getPathInfo() != null
        && request.getPathInfo().equals("/Cart")
        && request.getParameter("continue") == null) {
      request.setAttribute("ticket", "F-to-Cart");
      this.getServletContext().getNamedDispatcher("ShoppingCartServlet")
          .forward(request, response);
    }

    else if (request.getPathInfo() != null
        && request.getPathInfo().equals("/Analytics")) {
      request.setAttribute("ticket", "F-to-Analytics");
      ctx.getNamedDispatcher("AnalyticsServlet").forward(request, response);
    } else if (request.getPathInfo() != null
        && request.getPathInfo().equals("/Logout")) {
      request.setAttribute("ticket", "Front");
      ctx.getNamedDispatcher("LogoutServlet").forward(request, response);
    } else if (request.getPathInfo() != null
        && request.getPathInfo().equals("/Item")) {
      request.setAttribute("ticket", "Front-to-Item");
      ctx.getNamedDispatcher("ItemServlet").forward(request, response);
    } else {
      request.setAttribute("ticket", "Front");
      request.getRequestDispatcher("/Front.jspx").forward(request, response);
    }

  }

}
