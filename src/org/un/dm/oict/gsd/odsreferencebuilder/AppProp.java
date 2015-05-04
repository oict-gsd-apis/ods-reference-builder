package org.un.dm.oict.gsd.odsreferencebuilder;

import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Kevin Thomas Bradley
 * @dateCreated 4-May-2015
 * @description This class is used to store app variables used globally
 * @version 1.0
 * @codeReviewer
 */
public class AppProp {
	
	static String rootFileDirectory = "";
	
	static String solrInstance = "";
	static String solrCollection = "";
	static String solrUsername = "";
	static String solrPassword = "";
	static boolean writeSolrDocumentToSolr = false;
	
	static OutputDatabase database;
	static String databaseLocation = "";
	static String databaseName = "";
	static String databasePort = "";
	static String databaseUsername = "";
	static String databasePassword = "";
	static String databaseDocumentTable = "";
	static String databaseReferenceTable = "";
	static String databaseWarningTable = "";
	static String databaseErrorTable = "";
	
	static String referenceOutputFolder = "";
	static String documentOutputFolder = "";
	
	static String tikaTesseractServer = "";
	static String referenceRegex = "";
	static int pollDuration = 0;
	
	static Logger log;
	static Properties configFile;
	
}