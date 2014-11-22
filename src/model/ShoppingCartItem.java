package model;

import javax.xml.bind.annotation.XmlElement;

public class ShoppingCartItem {
	private String name;
	private int qty;
	private double price;
	
	public ShoppingCartItem(String name, int qty, double price) {
		this.name = name;
		this.qty = qty;
		this.price = price;
	}
	
	public ShoppingCartItem(String name, int qty) {
		this.name = name;
		this.qty = qty;
		this.price = 0.0;
	}
	
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
}
