package model;

public class ShoppingCartItem {
	private String itemName;
	private int qty;
	
	
	public ShoppingCartItem(String name, int qty) {
		this.itemName = name;
		this.qty = qty;
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
}
