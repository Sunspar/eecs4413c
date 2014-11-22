package model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="order")
public class ShoppingCartWrapper {
	
	@XmlAttribute(name="id")
	private long id;
	
	@XmlElement(name="items")
	private List<ShoppingCartItem> items;
	
	@XmlElement(name="total")
	private double subTotal;
	
	@XmlElement(name="shipping")
	private double shipping;
	
	@XmlElement(name="HST")
	private double hst;
	
	@XmlElement(name="grandTotal")
	private double grandTotal;
	
	public ShoppingCartWrapper(List<ShoppingCartItem> items) {
		this.id = 12345; // TODO: Generate proper order ID number
		this.items = items;

		this.subTotal = 0;
		for (ShoppingCartItem item: this.items){
			this.subTotal += item.getPrice() * item.getQuantity();
		}
		
		this.hst = 0.13 * (this.subTotal + this.shipping);
		this.shipping = 123.321; // TODO: Generate shipping properly
		this.grandTotal = this.subTotal + this.hst + this.shipping;
	}
	
	
	public long getOrderId() {
		return this.id;
	}
	
	private ShoppingCartWrapper() {} // Do not call this constructor.
}
