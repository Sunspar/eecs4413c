package model;

public class ItemBean {
  public String unit;
  public double costPrice;
  public int supID;
  public int catID;
  public int reorder;
  public int onorder;
  public int qty;
  public double price;
  public String name;
  public String number;

  /**
   * Creates a new ItemBean, which represents an item that we have for sale.
   * 
   * @param unit The unit type that this item is purchasable at its unit price by
   *        (e.g. each, per package, etc)
   * @param costPrice
   * @param supID
   * @param catID the category ID of this item
   * @param reorder
   * @param onorder
   * @param qty
   * @param price The unit price for this item
   * @param name The name of the item
   * @param number An item number which refers to this item independent of any other
   *        identifiers.
   */
  public ItemBean(String unit, double costPrice, int supID, int catID, int reorder,
      int onorder, int qty, double price, String name, String number) {
    this.unit = unit;
    this.costPrice = costPrice;
    this.supID = supID;
    this.catID = catID;
    this.reorder = reorder;
    this.onorder = onorder;
    this.qty = qty;
    this.price = price;
    this.name = name;
    this.number = number;
  }

  public String getUnit() {
    return unit;
  }

  public double getCostPrice() {
    return costPrice;
  }

  public int getSupID() {
    return supID;
  }

  public int getCatID() {
    return catID;
  }

  public int getReorder() {
    return reorder;
  }

  public int getOnorder() {
    return onorder;
  }

  public int getQty() {
    return qty;
  }

  public double getPrice() {
    return price;
  }

  public String getName() {
    return name;
  }

  public String getNumber() {
    return number;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public void setCostPrice(double costPrice) {
    this.costPrice = costPrice;
  }

  public void setSupID(int supID) {
    this.supID = supID;
  }

  public void setCatID(int catID) {
    this.catID = catID;
  }

  public void setReorder(int reorder) {
    this.reorder = reorder;
  }

  public void setOnorder(int onorder) {
    this.onorder = onorder;
  }

  public void setQty(int qty) {
    this.qty = qty;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setNumber(String number) {
    this.number = number;
  }

}
