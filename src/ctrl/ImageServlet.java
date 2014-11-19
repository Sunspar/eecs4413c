package ctrl;

import model.DAO;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

public class ImageServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        // This first line loads in the properties we have from the resources.values.properties file.
        // It contains the PARAM_IMAGE_ID key, which maps to the param string for image id's in all requests for images in the project
        ServletContext ctx = getServletContext();
        Properties props = (Properties) ctx.getAttribute(ctx.getInitParameter("PROPERTIES"));
        String id = request.getParameter(props.getProperty("PARAM_IMAGE_ID"));
        DAO dao = (DAO) ctx.getAttribute(props.getProperty("INTERNAL_DAO"));

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("[ImageServlet] Something called doPost() when it shouldn't have! ");
    }
}
