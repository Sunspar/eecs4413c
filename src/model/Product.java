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
	
	public ItemBean getItem(String name, String number) throws Exception {
		ItemBean item = null;
		if (name.equals("") || number.equals("")) {
			throw new InvalidParameterException("Invalid parameters!");
		}
		item = mDAO.getItem(name, number);
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

}
