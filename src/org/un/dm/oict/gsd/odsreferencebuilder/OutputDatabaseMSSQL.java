package org.un.dm.oict.gsd.odsreferencebuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kevin Thomas Bradley & Daniel Buenavad
 * @dateCreated 4-May-2015
 * @description This class is used as a connector to MSSQL DB
 * @version 1.0
 * @codeReviewer
 */
public class OutputDatabaseMSSQL extends OutputDatabase {
	
	public enum InfoType {
		Info,
		Warning,
		Error
	}
	 
	private Connection connection = null; 
	
	@Override
	/**
	 * This method is used to identify is a connection exists
	 * @return
	 */
	protected boolean isConnected() {
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

	@Override
	/**
	 * This method is used to establish a connection based on variables set in the config file
	 */
	protected void establishConnection() {
		
		try{
			String host = "jdbc:sqlserver://" + AppProp.databaseLocation + ":"+ AppProp.databasePort +";DatabaseName="+ AppProp.databaseName;

			// get the sql server database connection			
		    connection= DriverManager.getConnection(host,AppProp.databaseUsername,AppProp.databasePassword);
		}
		catch (SQLException err){
			System.out.println(err.getMessage());
		}
	}
	
	@Override
	/**
	 * This is an abstraction method used to insert a SolrDocument
	 * @param newSolrDocument
	 * @param folder
	 * @return
	 */
	protected boolean insertSolrDocument(SolrDocument newSolrDocument) {
		return insertDocument("Document", newSolrDocument.getId(), newSolrDocument.getSymbol(),  newSolrDocument.getLanguageCode(), newSolrDocument.getTitle(), newSolrDocument.getFilename().substring(0,newSolrDocument.getFilename().lastIndexOf("/")+1), newSolrDocument.getFilename().substring(newSolrDocument.getFilename().lastIndexOf("/")+1, newSolrDocument.getFilename().length()));
	}
	
	@Override
	/**
	 * 
	 */
	protected boolean insertReferenceDocument(ReferenceDocument newReferenceDocument) {
		return insertDocument("Reference", newReferenceDocument.getId(), newReferenceDocument.getSymbol(),  newReferenceDocument.getLanguageCode(), newReferenceDocument.getTitle(), newReferenceDocument.getFilename().substring(0,newReferenceDocument.getFilename().lastIndexOf("/")+1), newReferenceDocument.getFilename().substring(newReferenceDocument.getFilename().lastIndexOf("/")+1, newReferenceDocument.getFilename().length()));
	}
	
	@Override
	/**
	 * This method builds the query string, assigning parameters and calling the runQuery method
	 * @param documentType
	 * @param documentId
	 * @param documentSymbol
	 * @param language
	 * @param title
	 * @param folder
	 * @param fileName
	 * @return
	 */
	protected boolean insertDocument(String documentType, String documentId, String documentSymbol, String language, String title, String folder, String fileName) {
		try {			    
		    String qry = "INSERT INTO "+ (documentType == "Document"?  AppProp.databaseDocumentTable : AppProp.databaseReferenceTable) + " (\"DocumentId\", \"DocumentSymbol\", \"Language\", \"Title\", \"Folder\", \"Filename\") VALUES(?, ?, ?, ?, ?, ?)";
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
	
	@Override
	/**
	 * 
	 * @param documentId
	 * @param text
	 * @return
	 */
	protected boolean insertWarning(String documentId, String text) {
		return insertProblem(InfoType.Warning, documentId, text);
	}
	
	@Override
	/**
	 * 
	 * @param documentId
	 * @param text
	 * @return
	 */
	protected boolean insertError(String documentId, String text) {
		return insertProblem(InfoType.Error, documentId, text);
	}
	
	@Override
	/**
	 * This method like insertDocument is used to build the query string, assigning parameters and calling runQuery
	 */
	protected boolean insertProblem(InfoType problemType, String documentId, String text) {
		try {			    
		    String qry = "INSERT INTO "+ (problemType == InfoType.Warning ?  AppProp.databaseWarningTable : AppProp.databaseErrorTable) + " (\"DocumentId\", \"Text\") VALUES(?, ?)";
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
	
	@Override
	/**
	 * This is the actual method which makes the query call to the database
	 * @param query
	 * @param params
	 */
	protected void runQuery(String query, Map<Integer, Object> params) {
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
