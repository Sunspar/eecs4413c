package model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
  private ArrayList<ShoppingCartItem> cartContents;

  public ShoppingCart() {
    this.cartContents = new ArrayList<ShoppingCartItem>();
  }

  /**
   * Adds an item to the shopping cart. Adds to the item quantity if it is already
   * present in the cart.
   * 
   * @param item The name of the item to add to the cart
   * @param quantity The quantity of this item that should be added to the cart
   * @throws IllegalArgumentException if the quantity was not a valid integer
   */
  public void addItemToCart(String item, String number, String quantity)
      throws IllegalArgumentException {
    int idx = findCartItemPosition(item);
    int qty = extractIntegerType(quantity);

    if (idx >= 0) { // The item exists already, so lets just add to the current
                    // quantity
      ShoppingCartItem cartItem = this.cartContents.get(idx);
      cartItem.setQuantity(cartItem.getQuantity() + qty);
    } else { // The item doesnt exist, so lets just add it to the cart
      this.cartContents.add(new ShoppingCartItem(item, number, qty));
    }
  }

  /**
   * Modify the given item quantity to the new amount specified.
   * 
   * @param itemName The name of the item whose quantity should change
   * @param newQty the new quantity of the item
   */
  public void modifyItemQuantity(String itemName, String newQty) {
    int idx = findCartItemPosition(itemName);
    int newQuantity = extractIntegerType(newQty);
    if (idx >= 0) {
      this.cartContents.get(idx).setQuantity(newQuantity);
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
   * Returns the index of the item entry in the shopping cart matching the given
   * name. Returns an integer >=0 if found, and returns -1 otherwise.
   * 
   * @param itemName A {@link String} representation of the item name
   * @return -1 if no item with this name found, and a non-negative integer otherwise
   */
  private int findCartItemPosition(String itemName) {
    for (int idx = 0; idx < this.cartContents.size(); idx++) {
      if (this.cartContents.get(idx).getName().equals(itemName)) {
        return idx;
      }
    }

    return -1;
  }

  public List<ShoppingCartItem> getCartContents() {
    return this.cartContents;
  }

  /**
   * Returns the index'th item in the shopping cart. Returns null if index is out of
   * bounds.
   * 
   * @param index an index in the current shopping cart
   * @return A {@link ShoppingCartItem} found at the index'th position
   */
  public ShoppingCartItem getItem(int index) {
    if (index < 0)
      return null;
    if (index > this.cartContents.size())
      return null;
    return this.cartContents.get(index);
  }

  /**
   * Returns the number of items in this shopping cart.
   * 
   * @return the amount of items in the shopping cart
   */
  public int size() {
    return this.cartContents.size();
  }

  /**
   * Empties out the shopping cart.
   */
  public void empty() {
    this.cartContents = new ArrayList<ShoppingCartItem>();
  }

  /**
   * Private function to parse an integer type from a String.
   * 
   * @param intString the integer to be parsed, sent as a String
   * @return an integer representation of the argument String
   * @throws IllegalArgumentException if the String was not parsable to an integer
   *         type
   */
  private int extractIntegerType(String intString) throws IllegalArgumentException {
    try {
      return Integer.parseInt(intString);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Value was not a valid integer!");
    }
  }
}
