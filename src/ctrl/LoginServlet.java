package ctrl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;


public class LoginServlet extends HttpServlet {
	private final String USER_AGENT = "Mozilla/5.0";
	BigInteger n = new BigInteger(
			"945874683351289829816050197767812346183848578056570056860845622609107886220137"+
			"220709264916908438536900712481301344278323249667285825328323632215422317870682"+
			"037630270674000828353944598575250177072847684118190067762114937353265007829546"+
			"21660256501187035611332577696332459049538105669711385995976912007767106063");
	BigInteger e = new BigInteger("74327");
	
	public static String byteToHex(byte[] ar)
	{
		assert ar != null;
		String result = "";
		for (int i = 0; i < ar.length; i++)
		{
			int x = ar[i] & 0x000000FF;
			String tmp = Integer.toHexString(x);
			if (x < 16) tmp = "0" + tmp;
			result += tmp;
		}
		return result.toUpperCase();
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String target = "/Login.jspx";
	    
		req.getRequestDispatcher(target).forward(req, resp);
	}

	public String encrypt(String pt) throws Exception{
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(n, e);
		RSAPublicKey pubKey = (RSAPublicKey)keyFactory.generatePublic(pubKeySpec);

		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		
		byte[] ct = cipher.doFinal(pt.getBytes());
		return byteToHex(ct);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		
		if (username.length() > 10 || password.length() > 10){
			String target = "/Login.jspx";
		    req.setAttribute("error", 0);
			req.getRequestDispatcher(target).forward(req, resp);
		}
		
		
		String pt = username + ":" + password;
		
		try {
			String ct = encrypt(pt);
			String url = "http://www.cse.yorku.ca/~cse03257/auth.cgi?" + ct;
			System.out.println(url);
			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			// optional default is GET
			con.setRequestMethod("GET");
	 
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
	 
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String response = in.readLine();			
			in.close();
	 
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
				
				//print result
				System.out.println(session.getAttribute("name"));
				
				String target = "/e";
				req.getRequestDispatcher(target).forward(req, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
