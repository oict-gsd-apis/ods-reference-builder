package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.Map;

import org.un.dm.oict.gsd.odsreferencebuilder.OutputDatabaseMSSQL.InfoType;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 1-May-2015
 * @description This class is used to output a particular value to a database - abstract
 * @version 1.0
 * @codeReviewer
 */
public abstract class OutputDatabase {

	abstract boolean isConnected();
	
	abstract void establishConnection();
	
	abstract boolean insertDocument(String documentType, String documentId, String documentSymbol, String language, String title, String folder, String fileName);
	
	abstract boolean insertProblem(InfoType problemType, String documentId, String fileName, String text);
	
	abstract void runQuery(String query, Map<Integer, Object> params);
	
	abstract boolean insertSolrDocument(SolrDocument newSolrDocument);
	
	abstract boolean insertReferenceDocument(ReferenceDocument newReferenceDocument);
	
	abstract boolean insertWarning(String documentId, String documentName, String text);
	
	abstract boolean insertError(String documentId, String documentName, String text);
}
