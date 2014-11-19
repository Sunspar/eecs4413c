package model;

import java.util.ArrayList;

public class ShoppingCart {
	private ArrayList<Object[]> cartContents;
	
	public ShoppingCart() {
		this.cartContents = new ArrayList<Object[]>();
	}
	
	public void addItemToCart(String item, int quantity) {
		int idx = findCartItemPosition(item);
		
		if (idx >= 0) {
			this.cartContents.get(idx)[1] = (Integer) this.cartContents.get(idx)[1] + quantity;
		} else {
			this.cartContents.add(new Object[]{item, quantity});
		}
	}
	
	/**
	 * Returns the index of the item entry in the shopping cart matching the given name.
	 * Returns an integer >=0 if found, and returns -1 otherwise.
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
