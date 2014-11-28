package b2b;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	// get the price of an item from a specific provider
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
		
		Pattern pattern = Pattern.compile("quoteReturn(.*)quoteReturn");
		Matcher matcher = pattern.matcher(response.toString());
		if (matcher.find())
		{
			Pattern pattern2 = Pattern.compile(">(.*)<");
			Matcher matcher2 = pattern2.matcher(matcher.group(1));
			if (matcher2.find())
			{
				return Double.parseDouble(matcher2.group(1));
			}
		}
 
		return -1.0;
	}
	
	public double getLowestAvailable(double[] price){
		double low = price[0];
		
		for (int i=1; i< price.length; i++){
			if (low == -1.0){
				low = price[i];
			}
			else{
				if (price[i] != -1.0){
					low = Math.min(low, price[i]);
				}
			}
		}		
		return low;
	}
	
	public String getCompanyWtSuchPrice(String itemNo, double price) throws Exception{
		if (price == getPrice("YYZ", itemNo)){ //toronto
			return "YYZ";
		}
		if (price == getPrice("YVR", itemNo)){ //vancouver
			return "YVR";
		}
		if (price == getPrice("YHZ", itemNo)){ //halifax
			return "YHZ";
		}
		return "YYZ";
	}
	
	//return a map of itemName - quantity
	public HashMap<String, Integer> getRawOrder(){
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
	
	public HashMap<String, ArrayList<String>> 
		orderWtCompanyMap(HashMap<String, Integer> list) throws Exception{
		Map<String, ArrayList<String>> order = new HashMap<String, ArrayList<String>>();
		
		for (String itemNo : list.keySet()) {
			double[] options = new double[3];
			options[0] = getPrice("YYZ", itemNo);
			options[1] = getPrice("YVR", itemNo);
			options[2] = getPrice("YHZ", itemNo);
		    double price = getLowestAvailable(options);
		    if (price > -1.0){
		    	String company = getCompanyWtSuchPrice(itemNo, price);
		    	
		    	ArrayList<String> company_price = new ArrayList<String>();
			    company_price.add(company);
			    company_price.add(String.valueOf(price));
			    
			    order.put(itemNo, company_price);
		    }   
		    
		}
		
		return (HashMap<String, ArrayList<String>>) order;		
	}
	
	
	public void placeOrder(HashMap<String, ArrayList<String>> order){
		
	}
	
	public void genHTMLreport(HashMap<String, ArrayList<String>> order) throws Exception{
		long now = System.currentTimeMillis();
		PrintWriter writer = new PrintWriter("/eecs/home/cse03257/workspace/eecs4413c/report" + now + ".html", "UTF-8");
		writer.println("Procurement Report");
		writer.println("<!DOCTYPE html>	<html><body><table style=\"width:100%\" border=\"1\">");
		writer.println("<tr> <td>Item Number</td> <td>Company</td> <td>Price</td></tr>");
		
		for (String itemNo : order.keySet()) {
			String company = order.get(itemNo).get(0);
			String price = order.get(itemNo).get(1);
			writer.println("<tr> <td>" + itemNo + "</td> <td>"+company+"</td> <td>"+price+"</td></tr>"); 
		}
		
		writer.println("</table></body>	</html>");
		writer.close();
	}

	public static void main(String[] args) throws Exception {		 
		B2B b2b = new B2B("/eecs/home/cse03257/workspace/eecs4413c/orders");

		HashMap<String, Integer> list = b2b.getRawOrder();
		HashMap<String, ArrayList<String>> orderMapWtCompany = b2b.orderWtCompanyMap(list);
		
		System.out.println(orderMapWtCompany);
		
		long millis = System.currentTimeMillis();
		b2b.genHTMLreport(orderMapWtCompany);
		
		System.out.println(millis);
	}
}




























