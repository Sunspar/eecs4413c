package model;

import java.util.ArrayList;

public class ShoppingCart {
	private ArrayList<Object[]> cartContents;
	
	public ShoppingCart() {
		this.cartContents = new ArrayList<Object[]>();
	}
	
	/**
	 * Adds an item to the shopping cart. Adds to the item quantity if it is already present
	 * in the cart.
	 * 
	 * @param item The name of the item to add to the cart
	 * @param quantity The quantity of this item that should be added to the cart
	 */
	public void addItemToCart(String item, int quantity) {
		int idx = findCartItemPosition(item);
		
		if (idx >= 0) { // The item exists already, so lets just add to the current quantity
			this.cartContents.get(idx)[1] = (Integer) this.cartContents.get(idx)[1] + quantity;
		} else { // The item doesnt exist, so lets just add it to the cart
			this.cartContents.add(new Object[]{item, quantity});
		}
	}
	
	/**
	 * Modify the given item quantity to the new amount specified.
	 * 
	 * @param itemName The name of the item whose quantity should change
	 * @param newQty the new quantity of the item
	 */
	public void modifyItemQuantity(String itemName, int newQty) {
		int idx = findCartItemPosition(itemName);
		
		if (idx >= 0) {
			this.cartContents.get(idx)[1] = newQty;
		} else {
			// DO NOTHING, this was a malformed request or something
		}
	}
	
	/**
	 * Removes an item from the shopping cart.
	 * 
	 * @param itemName The name of the item to remove from the cart.
	 */
	public void removeItem(String itemName) {
		int idx = findCartItemPosition(itemName);
		
		if (idx >= 0) {
			this.cartContents.remove(idx);
		} else {
			// DO NOTHING, this item didnt exist.
		}
	}
	
	/**
	 * Returns the index of the item entry in the shopping cart matching the given name.
	 * Returns an integer >=0 if found, and returns -1 otherwise.
	 * 
	 * @param itemName A {@link String} representation of the item name 
	 * @return -1 if no item with this name found, and a non-negative integer otherwise
	 */
	private int findCartItemPosition(String itemName) {
		for (int idx = 0; idx < this.cartContents.size(); idx++) {
			if (this.cartContents.get(idx)[0].equals(itemName)) {
				return idx;
			}
		}
		
		return -1;
	}
}
