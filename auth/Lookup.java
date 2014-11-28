//package b2b;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Scanner;

import javax.crypto.Cipher;

public class Lookup {
	public static String path = "/etc/passwd";
	BigInteger n = new BigInteger(
			"945874683351289829816050197767812346183848578056570056860845622609107886220137"+
			"220709264916908438536900712481301344278323249667285825328323632215422317870682"+
			"037630270674000828353944598575250177072847684118190067762114937353265007829546"+
			"21660256501187035611332577696332459049538105669711385995976912007767106063");
	BigInteger e = new BigInteger("74327");
	BigInteger d = new BigInteger(
			"7289370196881601766768920490284861650464951706793000236386405648425161747775298"+
	      	"3441046583933853592091262678338882236956093668440986552405421520173544428836766"+
	      	"3419319185756836904299985444024205035318170370675348574916529512369448767695219"+
	      	"8090537385200990850805837963871485320168470788328336240930212290450023");
	
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
	
	public static byte[] hexToByte(String hex)
	{
		assert hex != null && hex.length() % 2 == 0;
		byte[] result = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i = i + 2)
		{
			result[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
		}
		return result;
	}
	
	public String getName(String username) throws Exception{
		//get the line containing such username
		Scanner sc = new Scanner(new File(path));
		while(sc.hasNextLine()){
		    String str = sc.nextLine();
		    
		    if (str.indexOf(username) == 0){
		    	try {
					String[] parts = str.split(":");
					
					return parts[4];
				} catch (Exception e) {
					return "no";
				}
		    }
		}
		
		return "no";
	}
	
	//takes in a hex string, return something like ngthuy:Fall2010
	public String decrypt(String ct) throws Exception{
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(n, d);
		RSAPrivateKey privKey = (RSAPrivateKey)keyFactory.generatePrivate(privKeySpec);

		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");		
		cipher.init(Cipher.DECRYPT_MODE, privKey);
		byte[] bk = cipher.doFinal(hexToByte(ct));
		
		return new String(bk);
	}
	
	public static void main(String[] args) throws Exception {
		if (args.length == 0){
			return;
		}
		
		Lookup lu = new Lookup();
		
		//-n means getName
		if (args[0].equals("-n")){
			System.out.println(lu.getName(args[1]));
		}
		
		if (args[0].equals("-d")){
			System.out.println(lu.decrypt(args[1]));
		}
	}
}
