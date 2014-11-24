package b2b;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class B2B {
	
	private final String key = "Mozilla/5.0";
	private final String USER_AGENT = "Mozilla/5.0";
	public String xmlPath = "/home/thao/workspace/eecs4413c/orders/";
	
	// HTTP GET request
	private void sendGet() throws Exception {
 
		String url = "http://roumani.eecs.yorku.ca:4413/axis/YYZ.jws?method=quote&itemNumber=0905A708";
 
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		con.setRequestMethod("GET");
 
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
	}
	
	public void readXML(){
		try {			 
			File fXmlFile = new File("/home/thao/workspace/eecs4413c/orders/PO1.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		 
			NodeList nList = doc.getElementsByTagName("item");
		 
			System.out.println("----------------------------");
		 
			for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				Node nNode = nList.item(temp);
		 
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
		 
					System.out.println("student id : " + eElement.getAttribute("number"));
					System.out.println("item Name : " + eElement.getElementsByTagName("name")
							.item(0).getTextContent());
				}
			}
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
	}

	public static void main(String[] args) throws Exception {		 
		B2B b2b = new B2B();
 
//		System.out.println("Testing 1 - Send Http GET request");
//		b2b.sendGet();
		
		b2b.readXML();
		
	}
}
