package model;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DAO {
  private DataSource ds;

  public DAO() throws NamingException {
    ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
  }

  /**
   * Queries the database for a list of items sold.
   * 
   * @return A list of {@link ItemBean}s, containing all items in the database
   * @throws SQLException if there was an error communicating with the database
   */
  public List<ItemBean> getAllItems() throws SQLException {
    List<ItemBean> result = new ArrayList<ItemBean>();
    Connection conn = this.ds.getConnection();

    String queryString = "";
    queryString += "SELECT *";
    queryString += " FROM ROUMANI.ITEMS";

    ResultSet rs = conn.createStatement().executeQuery(queryString);

    while (rs.next()) {
      // TODO Process the item into the bean class and further populate the result
      // list
    }
    return result;
  }

  /**
   * Queries the database for a list of items by Category
   * 
   * @return A list of {@link ItemBean}s, containing all items by category in the
   *         database
   * @throws SQLException if there was an error communicating with the database
   */
  public List<ItemBean> getItemsByCategory(int id) throws SQLException {
    List<ItemBean> result = new ArrayList<ItemBean>();
    Connection conn = this.ds.getConnection();

    String queryString = "";
    queryString += "SELECT *";
    queryString += " FROM ROUMANI.ITEM WHERE CATID=?";

    PreparedStatement ps = conn.prepareStatement(queryString);
    ps.setInt(1, id);

    ResultSet rs = ps.executeQuery();

    while (rs.next()) {
      ItemBean item =
          new ItemBean(rs.getString("unit"), rs.getDouble("costPrice"),
              rs.getInt("supID"), rs.getInt("catID"), rs.getInt("reorder"),
              rs.getInt("onorder"), rs.getInt("qty"), rs.getDouble("price"),
              rs.getString("name"), rs.getString("number"));
      result.add(item);
    }
    return result;
  }

  /**
   * Queries the database for a list of items based on search queries
   * 
   * @return A list of {@link ItemBean}s related to search query.
   * @throws SQLException if there was an error communicating with the database
   */
  public List<ItemBean> getItemsBySearch(String query) throws SQLException {
    List<ItemBean> result = new ArrayList<ItemBean>();
    Connection conn = this.ds.getConnection();

    String queryString = "";
    queryString += "SELECT *";
    queryString += " FROM ROUMANI.ITEM WHERE LOWER(NAME) LIKE LOWER(?)";

    PreparedStatement ps = conn.prepareStatement(queryString);
    ps.setString(1, "%" + query + "%");

    ResultSet rs = ps.executeQuery();

    while (rs.next()) {
      ItemBean item =
          new ItemBean(rs.getString("unit"), rs.getDouble("costPrice"),
              rs.getInt("supID"), rs.getInt("catID"), rs.getInt("reorder"),
              rs.getInt("onorder"), rs.getInt("qty"), rs.getDouble("price"),
              rs.getString("name"), rs.getString("number"));
      result.add(item);
    }
    return result;
  }

  /**
   * Queries the database for an item
   * 
   * @param number the item number of the queried item
   * @return An {@link ItemBean} to input name and number
   * @throws SQLException if there was an error communicating with the database
   */
  public ItemBean getItem(String number) throws SQLException {
    ItemBean result = null;
    Connection conn = this.ds.getConnection();

    String queryString = "";
    queryString += "SELECT *";
    queryString += " FROM ROUMANI.ITEM WHERE NUMBER LIKE ?";

    PreparedStatement ps = conn.prepareStatement(queryString);
    ps.setString(1, number);

    ResultSet rs = ps.executeQuery();

    while (rs.next()) {
      result =
          new ItemBean(rs.getString("unit"), rs.getDouble("costPrice"),
              rs.getInt("supID"), rs.getInt("catID"), rs.getInt("reorder"),
              rs.getInt("onorder"), rs.getInt("qty"), rs.getDouble("price"),
              rs.getString("name"), rs.getString("number"));
    }
    return result;
  }



  /**
   * Queries the database for the list of categories.
   * 
   * @return A list of {@link CategoryBean}s, containing all item categories in the
   *         database
   * @throws SQLException if there was an error communicating with the database
   */
  public List<CategoryBean> getAllCategories() throws SQLException {
    List<CategoryBean> result = new ArrayList<CategoryBean>();
    Connection conn = this.ds.getConnection();

    String queryString = "";
    queryString += "SELECT *";
    queryString += " FROM ROUMANI.CATEGORY";

    ResultSet rs = conn.createStatement().executeQuery(queryString);

    while (rs.next()) {
      CategoryBean category =
          new CategoryBean(rs.getInt("ID"), rs.getString("NAME"),
              rs.getString("DESCRIPTION"), rs.getBytes("PICTURE"));
      result.add(category);
    }

    return result;
  }

  public byte[] getCategoryImageById(String id) throws SQLException {
    String queryString = "";
    queryString += "SELECT PICTURE";
    queryString += " FROM ROUMANI.CATEGORY";
    queryString += " WHERE ID = ?";

    Connection conn = this.ds.getConnection();
    PreparedStatement ps = conn.prepareStatement(queryString);
    ps.setInt(1, extractIntegerType(id));

    ResultSet rs = ps.executeQuery();

    rs.next();
    Blob imageBlob = rs.getBlob(0);
    long length = imageBlob.length();
    return imageBlob.getBytes(0, (int) length);
  }

  /**
   * Queries for a specific item's price in the database.
   * 
   * @param itemName The name of the item to price check.
   * @return the price as a java double
   * @throws SQLException if there was an issue communicating with the database
   */
  public double getItemPrice(String itemName) throws SQLException {
    double result;
    String queryString = "";
    queryString += "SELECT PRICE";
    queryString += " FROM ROUMANI.ITEM";
    queryString += " WHERE NAME= ?";

    Connection conn = this.ds.getConnection();
    PreparedStatement ps = conn.prepareStatement(queryString);
    ps.setString(1, itemName);
    ResultSet rs = ps.executeQuery();
    rs.next();

    result = rs.getDouble(1);
    conn.close();

    return result;
  }

  private int extractIntegerType(String arg) throws SQLException {
    try {
      return Integer.parseInt(arg);
    } catch (NumberFormatException e) {
      throw new SQLException(e.getMessage(), e.getCause());
    }
  }

}
