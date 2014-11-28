package model;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

/* Main model */
public class Product {

  private DAO mDAO;

  public Product() throws NamingException {
    mDAO = new DAO();
  }

  public List<ItemBean> getItemsByCategory(String id) throws Exception {
    int mID = this.extractID(id);

    List<ItemBean> list = mDAO.getItemsByCategory(mID);
    return list;
  }

  public List<ItemBean> getItemsBySearch(String query) throws SQLException {
    List<ItemBean> list = null;
    if (query.equals("")) {
      System.out.println("[Product.java] reached!");
      return list;
    }
    list = mDAO.getItemsBySearch(query);
    return list;
  }

  public ItemBean getItem(String number) throws Exception {
    ItemBean item = null;
    if (number.equals("")) {
      throw new InvalidParameterException("Invalid parameters!");
    }
    item = mDAO.getItem(number);
    if (item == null) {
      throw new InvalidParameterException("Item does not exist!");
    }
    return item;
  }

  /** Parse function **/
  private int extractID(String id) throws IllegalArgumentException {
    int result = 0;
    try {
      result = Integer.parseInt(id);
    } catch (Exception e) {
      throw new IllegalArgumentException("ID not an int!");
    }
    return result;
  }

  public List<CategoryBean> getAllCategories() throws SQLException {
    List<CategoryBean> list = mDAO.getAllCategories();
    return list;
  }

  public double getItemPrice(String itemName) throws SQLException {
    return mDAO.getItemPrice(itemName);
  }

  public double calculateShippingRate(List<ShoppingCartItem> cartContents) {
    final double cap = 100.0;
    final double free = 0.0;
    final double standard = 5.0;
    double total = 0.0;
    for (int i = 0; i < cartContents.size(); i++) {
      total +=
          (cartContents.get(i).getPrice()) * (cartContents.get(i).getQuantity());
    }

    return (total >= cap) ? free : standard;
  }

  public double getTaxRate() {
    final double tax = 0.13;
    return tax;
  }

}
