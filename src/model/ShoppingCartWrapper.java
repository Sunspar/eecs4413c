package model;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="order")
@XmlType(propOrder = {"customer", "subTotal", "shipping", "hst", "grandTotal", "items"})
public class ShoppingCartWrapper {
	
	@XmlAttribute
	private long id;

	@XmlAttribute
	String submitted;
	
	@XmlElement(name="customer")
	CustomerBean customer;
	
	@XmlElement(name="item")
	@XmlElementWrapper(name="items")
	private List<ShoppingCartItem> items;
	
	@XmlElement(name="total")
	private double subTotal;
	
	@XmlElement(name="shipping")
	private double shipping;
	
	@XmlElement(name="HST")
	private double hst;
	
	@XmlElement(name="grandTotal")
	private double grandTotal;
	
	public ShoppingCartWrapper(CustomerBean customer, List<ShoppingCartItem> items) {
		this.customer = customer;
		Date dateObj = new Date();
		String date = dateObj.getYear() + "-" + dateObj.getMonth() + "-" + dateObj.getDate();
		this.id = 12345; // TODO: Generate proper order ID number
		this.items = items;

		this.subTotal = 0;
		for (ShoppingCartItem item: this.items) {
			this.subTotal += item.getPrice() * item.getQuantity();
		}
		
		if (this.subTotal >= 100.00) {
			this.shipping = 0.00;
		} else {
			this.shipping = 5.00;
		}
		
		this.hst = 0.13 * (this.subTotal + this.shipping);
		this.grandTotal = this.subTotal + this.hst + this.shipping;
	}
	
	
	public long getOrderId() {
		return this.id;
	}
	
	public String getSubmitted() {
		return this.submitted;
	}
	
	private ShoppingCartWrapper() {} // Do not call this constructor.
}
