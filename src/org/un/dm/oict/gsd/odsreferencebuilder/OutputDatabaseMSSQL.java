package org.un.dm.oict.gsd.odsreferencebuilder;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class OutputDatabaseMSSQL extends OutputDatabase {
	 
	private Connection connection = null; 
	
	
	/**
	 * 
	 * @return
	 */
	public boolean isConnected() {
		try {
			if (!connection.isClosed())
				return true;
			else
				return false;
		} catch (SQLException e) {
			System.out.println("ERROR: Checking if connected to SQL: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * 
	 */
	public void establishConnection() {
		
		try{
			String host = "jdbc:sqlserver://" + AppProp.databaseLocation + ":"+ AppProp.databasePort +";DatabaseName="+ AppProp.databaseName;

			// get the sql server database connection			
		    connection= DriverManager.getConnection(host,AppProp.databaseUsername,AppProp.databasePassword);
		}
		catch (SQLException err){
			System.out.println(err.getMessage());
		}
	}
	
	/**
	 * 
	 * @param documentType
	 * @param documentId
	 * @param documentSymbol
	 * @param language
	 * @param title
	 * @param folder
	 * @param fileName
	 * @return
	 */
	public boolean insertDocument(String documentType, String documentId, String documentSymbol, String language, String title, String folder, String fileName) {
		try {			    
		    String qry = "INSERT INTO \""+ (documentType == "Document"?  AppProp.databaseDocumentTable : AppProp.databaseReferenceTable) + "\" (\"DocumentId\", \"DocumentSymbol\", \"Language\", \"Title\", \"Folder\", \"Filename\") VALUES(?, ?, ?, ?, ?, ?)";
		    Map<Integer, Object> params = new HashMap<Integer, Object>();
		    params.put(1, documentId);
		    params.put(2, documentSymbol);
		    params.put(3, language);
		    params.put(4, title);
		    params.put(5, folder);
		    params.put(6, fileName);

		    runQuery(qry, params);
			
			return true;
			
		} catch (Exception e) {
			System.out.println("ERROR: Inserting record to SQL "+ documentType +" table " + e.getMessage());
			return false;
		}
	}
	
	public boolean insertProblem(String problemType, String documentId, String text) {
		try {			    
		    String qry = "INSERT INTO \""+ (problemType == "Warning"?  AppProp.databaseWarningTable : AppProp.databaseErrorTable) + "\" (\"DocumentId\", \"Text\",) VALUES(?, ?)";
		    Map<Integer, Object> params = new HashMap<Integer, Object>();
		    params.put(1, documentId);
		    params.put(2, text);
		    runQuery(qry, params);
			
			return true;
			
		} catch (Exception e) {
			System.out.println("ERROR: Inserting record to SQL "+ problemType +" table " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * 
	 * @param query
	 * @param params
	 */
	public void runQuery(String query, Map<Integer, Object> params) {
		PreparedStatement pst;
		try {
			pst = this.connection.prepareStatement(query);
			for (Map.Entry<Integer, Object> kv : params.entrySet()) {
				if (kv.getValue().getClass().isAssignableFrom(String.class)) {
					pst.setString(kv.getKey(), (String) kv.getValue());
				}
				if (kv.getValue().getClass().isAssignableFrom(Timestamp.class)) {
					pst.setTimestamp(kv.getKey(), (Timestamp) kv.getValue());
				}	
			}
			pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("ERROR: Attempting to run query (" + query + "): " + e.getMessage());
		}			
	}

}
