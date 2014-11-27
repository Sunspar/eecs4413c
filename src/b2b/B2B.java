package b2b;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public String xmlPath;
	
	public B2B(String path){
		xmlPath = path;
	}
	
	// HTTP GET request
	private double getPrice(String company, String itemNo) throws Exception { 
		//String url = "http://roumani.eecs.yorku.ca:4413/axis/YYZ.jws?method=quote&itemNumber=0905A708"; 
		String url = "http://roumani.eecs.yorku.ca:4413/axis/" + company +".jws?method=quote&itemNumber=" + itemNo; 
	
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		con.setRequestMethod("GET"); 
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//Pattern pattern = Pattern.compile(">(.*)<");
		Pattern pattern = Pattern.compile("quoteReturn(.*)quoteReturn");
		Matcher matcher = pattern.matcher(response.toString());
		if (matcher.find())
		{
		    //System.out.println(matcher.group(1));
			Pattern pattern2 = Pattern.compile(">(.*)<");
			Matcher matcher2 = pattern2.matcher(matcher.group(1));
			if (matcher2.find())
			{
				return Double.parseDouble(matcher2.group(1));
			}
		}
 
		return -1.0;
	}
	
	//return a map of itemname - quantity
	public HashMap readXMLreports(){
		Map<String, Integer> list = new HashMap<String, Integer>();		
		File xmlFolder = new File(xmlPath);
		
		for (File file : xmlFolder.listFiles()) {
		    //System.out.println(file);
		    try {			 
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(file);		 
				doc.getDocumentElement().normalize();
			 
				NodeList nList = doc.getElementsByTagName("item");
			 
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
			 
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						
						String itemNo = eElement.getAttribute("number");
						int quantity = Integer.parseInt(eElement.getElementsByTagName("quantity")
								.item(0).getTextContent());
						
						if (list.get(itemNo) == null){
							list.put(itemNo, quantity);
						}
						else{
							int newQuant = (int)list.get(itemNo) + quantity;
							list.put(itemNo, newQuant);
						}
					}
				}
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		}
		
		System.out.println(list);
		return (HashMap<String, Integer>) list;
	}
	
	public void order(){
		
	}

	public static void main(String[] args) throws Exception {		 
		B2B b2b = new B2B("/eecs/home/cse03257/workspace/eecs4413c/orders");
		//public String xmlPath = "/home/thao/workspace/eecs4413c/orders/";

		b2b.readXMLreports();
		System.out.println(b2b.getPrice("YYZ", "0905A708"));
		System.out.println(b2b.getPrice("YVR", "0905A708"));
	}
}
