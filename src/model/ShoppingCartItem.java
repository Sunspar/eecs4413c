package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"name", "price", "quantity", "extended"})
public class ShoppingCartItem {
	private String number;
	private String name;
	private int qty;
	private double price;
	
	public ShoppingCartItem(String name, String number, int qty, double price) {
		this.name = name;
		this.number = number;
		this.qty = qty;
		this.price = price;
	}
	
	public ShoppingCartItem(String name, String number, int qty) {
		this.name = name;
		this.number = number;
		this.qty = qty;
		this.price = 0.0;
	}
	
	private ShoppingCartItem() {} // Used for XML marshalling, do not use this constructor.
	
	public void setQuantity(int newQty) {
		this.qty = newQty;
	}
	
	public int getQuantity() {
		return this.qty;
	}
	
	public void setName(String newName) {
		this.name = newName;
	}
	
	public String getName() {
		return this.name;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}

	@XmlElement
	public double getExtended() {
		return this.price * this.qty;
	}
	
	@XmlAttribute
	public String getNumber() {
		return this.number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
}
