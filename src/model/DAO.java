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
			//TODO Process the item into the bean class and further populate the result list
		}
		
		return result;
	}
	
	/**
	 * Queries the database for the list of categories.
	 * 
	 * @return A list of {@link CategoryBean}s, containing all item categories in the database
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
			//TODO Process the category into the bean class and further populate the result list
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
    
    private int extractIntegerType(String arg) throws SQLException {
    	try {
    		return Integer.parseInt(arg);
    	} catch (NumberFormatException e) {
    		throw new SQLException(e.getMessage(), e.getCause());
    	}
    }
}
