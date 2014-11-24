package ctrl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;

public class LoginServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		this.doPost(req, resp);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			URL url = new URL("http://www.cse.yorku.ca/~cse03257/auth.cgi");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			String userpass = "andrew:andrew";
			String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
			connection.setRequestProperty("Authorization", basicAuth);
			connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.01");
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			connection.setRequestProperty("User-Agent", "eFoods Login Servlet");
			connection.setRequestProperty("Content-Length", "0");
			connection.setRequestProperty("Content-Type", "text/html");
			int rc = connection.getResponseCode();
			
			StringBuffer authResponse = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine = "";
			while ((inputLine = in.readLine()) != null) {
				authResponse.append(inputLine);
			}
		
			System.out.println(authResponse.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
