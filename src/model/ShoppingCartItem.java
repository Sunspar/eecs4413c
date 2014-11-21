package model;

public class ShoppingCartItem {
	private String itemName;
	private int qty;
	private double price;
	
	
	public ShoppingCartItem(String name, int qty, double price) {
		this.itemName = name;
		this.qty = qty;
		this.price = price;
	}
	
	public ShoppingCartItem(String name, int qty) {
		this.itemName = name;
		this.qty = qty;
		this.price = 0.0;
	}
	
	public void setQuantity(int newQty) {
		this.qty = newQty;
	}
	
	public int getQuantity() {
		return this.qty;
	}
	
	public String getItemName() {
		return this.itemName;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
}
