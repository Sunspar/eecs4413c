package ctrl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;


public class LoginServlet extends HttpServlet {
	private final String USER_AGENT = "Mozilla/5.0";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String target = "/Login.jspx";
	    
		req.getRequestDispatcher(target).forward(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String url = "http://www.cse.yorku.ca/~cse03257/auth.cgi?" + username +":" + password;
		
		try {			 
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			// optional default is GET
			con.setRequestMethod("GET");
	 
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
	 
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String response = in.readLine();			
			in.close();
	 
			//print result
			System.out.println(response.toString());
			
			//login fails
			if (response.equals("no")){
				String target = "/Login.jspx";
			    req.setAttribute("error", 0);
				req.getRequestDispatcher(target).forward(req, resp);
			}
			else{
				HttpSession session = req.getSession();
				session.setAttribute("name", response.toString());
				session.setAttribute("cse", username);
				
				String target = "/e";
				req.getRequestDispatcher(target).forward(req, resp);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
